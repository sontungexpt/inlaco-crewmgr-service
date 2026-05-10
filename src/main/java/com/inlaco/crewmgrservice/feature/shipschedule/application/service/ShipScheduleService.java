package com.inlaco.crewmgrservice.feature.shipschedule.application.service;

import com.inlaco.crewmgrservice.feature.contract.application.port.out.CrewSupplyContractRepository;
import com.inlaco.crewmgrservice.feature.contract.domain.model.Contract;
import com.inlaco.crewmgrservice.feature.contract.domain.model.party.Party;
import com.inlaco.crewmgrservice.feature.crew.application.port.in.CrewUseCase;
import com.inlaco.crewmgrservice.feature.crew.domain.model.CrewProfile;
import com.inlaco.crewmgrservice.feature.shipschedule.application.mapper.ShipScheduleDetailMapper;
import com.inlaco.crewmgrservice.feature.shipschedule.application.model.ShipScheduleAssignedCrewDetail;
import com.inlaco.crewmgrservice.feature.shipschedule.application.model.ShipScheduleDetail;
import com.inlaco.crewmgrservice.feature.shipschedule.application.model.ShipScheduleSearchCriteria;
import com.inlaco.crewmgrservice.feature.shipschedule.application.port.in.ShipScheduleUseCase;
import com.inlaco.crewmgrservice.feature.shipschedule.application.port.out.ShipScheduleCrewAssignmentRepository;
import com.inlaco.crewmgrservice.feature.shipschedule.application.port.out.ShipScheduleRepository;
import com.inlaco.crewmgrservice.feature.shipschedule.domain.model.ShipSchedule;
import com.inlaco.crewmgrservice.feature.shipschedule.domain.model.ShipScheduleCrewAssignment;
import com.inlaco.crewmgrservice.feature.upload.application.port.in.UploadDispatcher;
import com.inlaco.crewmgrservice.feature.upload.domain.enums.AssetType;
import com.inlaco.crewmgrservice.feature.user.domain.model.User;
import com.inlaco.crewmgrservice.shared.kernel.exception.ResourceNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShipScheduleService implements ShipScheduleUseCase {

  private final ShipScheduleRepository shipScheduleRepository;
  private final ShipScheduleCrewAssignmentRepository assignmentRepository;
  private final CrewUseCase crewUseCase;
  private final ShipScheduleDetailMapper detailMapper;
  private final UploadDispatcher uploadDispatcher;
  private final CrewSupplyContractRepository crewSupplyContractRepository;

  @Override
  public ShipSchedule createSchedule(
      ShipSchedule schedule, List<ShipScheduleCrewAssignment> assignments, User authenticatedUser) {
    // Validate ship IMO against active contracts
    validateShipImoAgainstContracts(schedule, authenticatedUser);

    List<String> employeeCardIds =
        assignments.stream().map(ShipScheduleCrewAssignment::getEmployeeCardId).toList();

    List<CrewProfile> crewProfiles = crewUseCase.getProfilesByEmployeeCardIds(employeeCardIds);

    if (crewProfiles.size() != employeeCardIds.size()) {
      throw new IllegalArgumentException("Some crew members do not exist");
    }

    Map<String, CrewProfile> crewProfileMap =
        crewProfiles.stream()
            .collect(Collectors.toMap(CrewProfile::getEmployeeCardId, Function.identity()));

    enrichSchedule(schedule, assignments);
    enrichAssignments(assignments, crewProfileMap);

    ShipSchedule created = shipScheduleRepository.save(schedule);
    assignments.forEach(assignment -> assignment.setScheduleId(created.getId()));

    return created;
  }

  private void validateShipImoAgainstContracts(ShipSchedule schedule, User authenticatedUser) {
    String shipImoNumber = schedule.getShipInfo().getImoNumber();
    if (shipImoNumber == null || shipImoNumber.trim().isEmpty()) {
      throw new IllegalArgumentException("Ship IMO number is required");
    }

    List<Contract> activeContracts =
        crewSupplyContractRepository.findActiveContractsByShipIMO(shipImoNumber);

    if (activeContracts.isEmpty()) {
      throw new IllegalArgumentException(
          String.format(
              "Ship with IMO number %s is not found in any active deployment contracts",
              shipImoNumber));
    }

    for (var contract : activeContracts) {
      Party party = contract.getPartners().get(0);

      if (!party.getAccountId().equals(authenticatedUser.getId())) {
        throw new IllegalArgumentException(
            String.format(
                "Ship with IMO number %s is not found in any active deployment contracts",
                shipImoNumber));
      }
    }

    log.debug("Found {} active contracts for ship IMO: {}", activeContracts.size(), shipImoNumber);
  }

  private void enrichSchedule(ShipSchedule schedule, List<ShipScheduleCrewAssignment> assignments) {
    log.debug("Enriching schedule");
    schedule
        .getShipInfo()
        .setImage(uploadDispatcher.enrich(AssetType.SHIP_IMAGE, schedule.getShipInfo().getImage()));
  }

  private void enrichAssignments(
      List<ShipScheduleCrewAssignment> assignments, Map<String, CrewProfile> profileMap) {
    log.debug("Enriching assignments");
    for (ShipScheduleCrewAssignment assignment : assignments) {

      CrewProfile profile = profileMap.get(assignment.getEmployeeCardId());

      if (profile == null) {
        throw new IllegalArgumentException("Crew profile not found");
      }

      if (profile.getAccountId() == null) {
        throw new IllegalArgumentException("Crew member has no account");
      }

      assignment.setProfileId(profile.getId());
      assignment.setAccountId(profile.getAccountId());

      assignment.setFullName(profile.getFullName());
    }
  }

  @Override
  public Page<ShipSchedule> getSchedules(ShipScheduleSearchCriteria criteria, Pageable pageable) {
    log.debug("Getting schedules");
    return shipScheduleRepository.findAll(criteria, pageable);
  }

  @Override
  public ShipScheduleDetail getScheduleDetail(String scheduleId) {
    ShipSchedule schedule = getSchedule(scheduleId);
    List<ShipScheduleCrewAssignment> assignments =
        assignmentRepository.findByScheduleId(scheduleId);

    List<String> employeeCardIds = new ArrayList<>(assignments.size());
    for (var ac : assignments) {
      employeeCardIds.add(ac.getEmployeeCardId());
    }

    // Fetch profiles
    log.debug("Fetching crew profiles with employeeCardIds: {}", employeeCardIds);
    var profiles = crewUseCase.getProfilesByEmployeeCardIds(employeeCardIds);

    // Build profile map
    Map<String, CrewProfile> profileMap = new HashMap<>(profiles.size());
    for (CrewProfile profile : profiles) {
      profileMap.put(profile.getEmployeeCardId(), profile);
    }

    // Build crew details
    log.debug("Building crew details");
    List<ShipScheduleAssignedCrewDetail> crewDetails = new ArrayList<>(assignments.size());

    for (var ac : assignments) {
      CrewProfile profile = profileMap.get(ac.getEmployeeCardId());

      ShipScheduleAssignedCrewDetail detail = new ShipScheduleAssignedCrewDetail();

      // schedule data
      detail.setEmployeeCardId(ac.getEmployeeCardId());
      detail.setRankOnBoard(ac.getRankOnBoard());

      // profile data
      if (profile != null) {
        detail.setProfileId(profile.getId());
        detail.setFullName(profile.getFullName());
        detail.setEmail(profile.getEmail());
        detail.setPhoneNumber(profile.getPhoneNumber());
        detail.setAddress(profile.getAddress());
        detail.setGender(profile.getGender());
      }

      crewDetails.add(detail);
    }

    return detailMapper.toDetail(schedule, crewDetails);
  }

  @Override
  public ShipSchedule getSchedule(String scheduleId) {
    return shipScheduleRepository
        .findById(scheduleId)
        .orElseThrow(() -> new ResourceNotFoundException(ShipSchedule.class, "id", scheduleId));
  }
}

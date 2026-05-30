package com.inlaco.crewmgrservice.feature.shipschedule.application.service;

import com.inlaco.crewmgrservice.feature.contract.application.port.out.CrewSupplyContractRepository;
import com.inlaco.crewmgrservice.feature.contract.domain.model.Contract;
import com.inlaco.crewmgrservice.feature.crew.application.port.in.CrewUseCase;
import com.inlaco.crewmgrservice.feature.crew.domain.model.CrewProfile;
import com.inlaco.crewmgrservice.feature.crewmobilization.application.port.in.CrewMobilizationQueryUseCase;
import com.inlaco.crewmgrservice.feature.crewmobilization.domain.model.CrewMobilizationAssignment;
import com.inlaco.crewmgrservice.feature.shipschedule.application.mapper.ShipScheduleDetailMapper;
import com.inlaco.crewmgrservice.feature.shipschedule.application.model.ShipScheduleAssignedCrewDetail;
import com.inlaco.crewmgrservice.feature.shipschedule.application.model.ShipScheduleDetail;
import com.inlaco.crewmgrservice.feature.shipschedule.application.model.ShipScheduleSearchCriteria;
import com.inlaco.crewmgrservice.feature.shipschedule.application.port.in.ShipScheduleUseCase;
import com.inlaco.crewmgrservice.feature.shipschedule.application.port.out.ShipScheduleCrewAssignmentRepository;
import com.inlaco.crewmgrservice.feature.shipschedule.application.port.out.ShipScheduleRepository;
import com.inlaco.crewmgrservice.feature.shipschedule.application.service.CrewAssignmentBusyException.ConflictAssignment;
import com.inlaco.crewmgrservice.feature.shipschedule.domain.error.ShipScheduleErrorCode;
import com.inlaco.crewmgrservice.feature.shipschedule.domain.event.ShipScheduleCreatedEvent;
import com.inlaco.crewmgrservice.feature.shipschedule.domain.exception.ShipScheduleException;
import com.inlaco.crewmgrservice.feature.shipschedule.domain.model.ShipSchedule;
import com.inlaco.crewmgrservice.feature.shipschedule.domain.model.ShipScheduleCrewAssignment;
import com.inlaco.crewmgrservice.feature.upload.application.port.in.UploadDispatcher;
import com.inlaco.crewmgrservice.feature.upload.domain.enums.AssetType;
import com.inlaco.crewmgrservice.feature.user.domain.model.User;
import com.inlaco.crewmgrservice.shared.kernel.exception.ResourceNotFoundException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShipScheduleService implements ShipScheduleUseCase {

  private final ShipScheduleRepository shipScheduleRepository;
  private final ShipScheduleCrewAssignmentRepository assignmentRepository;
  private final CrewMobilizationQueryUseCase mobilizationQueryUseCase;
  private final CrewUseCase crewUseCase;
  private final ShipScheduleDetailMapper detailMapper;
  private final UploadDispatcher uploadDispatcher;
  private final CrewSupplyContractRepository crewSupplyContractRepository;
  private final ApplicationEventPublisher eventPublisher;

  @Override
  @Transactional
  public ShipSchedule createSchedule(
      ShipSchedule schedule, List<ShipScheduleCrewAssignment> assignments, User user) {

    validateShipImoAndContract(schedule, user);

    validateAssignments(assignments);

    List<String> employeeCardIds =
        assignments.stream().map(ShipScheduleCrewAssignment::getEmployeeCardId).toList();

    List<CrewProfile> crewProfiles = crewUseCase.getProfilesByEmployeeCardIds(employeeCardIds);

    validateCrewExist(employeeCardIds, crewProfiles);

    Map<String, CrewProfile> crewMap =
        crewProfiles.stream()
            .collect(Collectors.toMap(CrewProfile::getEmployeeCardId, Function.identity()));

    enrichSchedule(schedule, user);

    ShipSchedule saved = shipScheduleRepository.save(schedule);
    validateAssignmentsOverlap(assignments, saved, user);
    enrichAssignments(assignments, crewMap, saved);

    assignmentRepository.saveAll(assignments);

    // Publish event for ship schedule creation
    eventPublisher.publishEvent(new ShipScheduleCreatedEvent(saved, assignments, crewProfiles));

    return saved;
  }

  private void validateShipImoAndContract(ShipSchedule schedule, User user) {

    String imo = schedule.getShipInfo().getImoNumber();

    if (imo == null || imo.isBlank()) {
      throw new ShipScheduleException(
          ShipScheduleErrorCode.SHIP_IMO_NUMBER_REQUIRED, "IMO is required");
    }

    List<Contract> contracts = crewSupplyContractRepository.findActiveContractsByShipIMO(imo);

    boolean authorized =
        contracts.stream()
            .flatMap(c -> c.getPartners().stream())
            .anyMatch(p -> p.getAccountId().equals(user.getId()));

    if (!authorized) {
      throw new ShipScheduleException(
          ShipScheduleErrorCode.SHIP_NOT_AUTHORIZED_FOR_USER, "User not authorized for this ship");
    }

    if (contracts.isEmpty()) {
      throw new ShipScheduleException(
          ShipScheduleErrorCode.SHIP_NOT_FOUND_IN_ACTIVE_CONTRACTS, "No active contract found");
    }

    log.debug("Found {} active contracts for ship IMO: {}", contracts.size(), imo);
  }

  private void validateCrewExist(List<String> ids, List<CrewProfile> profiles) {

    Set<String> found =
        profiles.stream().map(CrewProfile::getEmployeeCardId).collect(Collectors.toSet());

    List<String> missing = ids.stream().filter(id -> !found.contains(id)).toList();

    if (!missing.isEmpty()) {
      throw new ShipScheduleException(
          ShipScheduleErrorCode.CREW_PROFILE_NOT_FOUND, "Missing crew: " + missing, missing);
    }
  }

  private void validateAssignmentsOverlap(
      List<ShipScheduleCrewAssignment> assignments, ShipSchedule shipSchedule, User user) {

    String shipIMO = shipSchedule.getShipInfo().getImoNumber();

    List<String> employeeCardIds =
        assignments.stream().map(ShipScheduleCrewAssignment::getEmployeeCardId).toList();

    /*
     * EXISTING ASSIGNMENTS (same ship + same crew)
     */
    List<CrewMobilizationAssignment> existingAssignments =
        mobilizationQueryUseCase.findAllActiveAssignmentsForClientWithShipIMOAndEmployeeCardIds(
            shipIMO, user.getId(), employeeCardIds);

    /*
     * GROUP existing by employee
     */
    Map<String, List<CrewMobilizationAssignment>> existingByCrew =
        existingAssignments.stream()
            .collect(Collectors.groupingBy(CrewMobilizationAssignment::getEmployeeCardId));

    List<ConflictAssignment> mobilizationConflicts = new ArrayList<>();

    /*
     * CHECK EACH NEW ASSIGNMENT
     */
    for (ShipScheduleCrewAssignment newAssignment : assignments) {

      String employeeCardId = newAssignment.getEmployeeCardId();

      List<CrewMobilizationAssignment> existingList =
          existingByCrew.getOrDefault(employeeCardId, List.of());

      for (CrewMobilizationAssignment existing : existingList) {

        boolean overlap =
            newAssignment.getBoardingTime().isBefore(existing.getEndDate())
                && newAssignment.getDisembarkTime().isAfter(existing.getStartDate());

        if (overlap) {

          mobilizationConflicts.add(
              new ConflictAssignment(
                  employeeCardId,
                  "Crew already assigned on overlapping schedule",
                  existing.getStartDate(),
                  existing.getEndDate()));
        }
      }
    }

    /*
     * THROW IF ANY CONFLICT
     */
    if (!mobilizationConflicts.isEmpty()) {
      throw new CrewAssignmentBusyException(mobilizationConflicts);
    }
  }

  private void validateAssignments(List<ShipScheduleCrewAssignment> assignments) {

    if (assignments == null || assignments.isEmpty()) {
      throw new ShipScheduleException(
          ShipScheduleErrorCode.CREW_PROFILE_NOT_FOUND, "Assignments empty");
    }

    for (var a : assignments) {

      if (a.getBoardingTime() == null || a.getDisembarkTime() == null) {
        throw new ShipScheduleException(ShipScheduleErrorCode.INVALID_ASSIGNMENT_DATE, "Date null");
      }

      if (!a.getBoardingTime().isBefore(a.getDisembarkTime())) {
        throw new ShipScheduleException(
            ShipScheduleErrorCode.INVALID_ASSIGNMENT_DATE, "Invalid time range");
      }
    }
  }

  private void enrichSchedule(ShipSchedule schedule, User user) {
    log.debug("Enriching schedule");

    schedule.setVesselOwnerId(user.getId());
    schedule
        .getShipInfo()
        .setImage(uploadDispatcher.enrich(AssetType.SHIP_IMAGE, schedule.getShipInfo().getImage()));
  }

  private void enrichAssignments(
      List<ShipScheduleCrewAssignment> assignments,
      Map<String, CrewProfile> crewMap,
      ShipSchedule shipSchedule) {

    log.debug("Enriching assignments");

    for (var a : assignments) {

      CrewProfile p = crewMap.get(a.getEmployeeCardId());

      if (p == null) {
        throw new ShipScheduleException(
            ShipScheduleErrorCode.CREW_PROFILE_NOT_FOUND, "Crew not found");
      }

      if (p.getAccountId() == null) {
        throw new ShipScheduleException(
            ShipScheduleErrorCode.CREW_MEMBER_HAS_NO_ACCOUNT, "No account");
      }

      a.setProfileId(p.getId());
      a.setAccountId(p.getAccountId());
      a.setFullName(p.getFullName());

      a.setVesselOwnerId(shipSchedule.getVesselOwnerId());
      a.setShipIMO(shipSchedule.getShipInfo().getImoNumber());
      a.setScheduleId(shipSchedule.getId());
    }
  }

  @Override
  public ShipScheduleDetail getScheduleDetail(String scheduleId) {
    log.info("Fetching schedule detail with id: {}", scheduleId);
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
    log.info("Fetching ship schedule with id: {}", scheduleId);
    return shipScheduleRepository
        .findById(scheduleId)
        .orElseThrow(() -> new ResourceNotFoundException(ShipSchedule.class, "id", scheduleId));
  }

  @Override
  public Page<ShipSchedule> getSchedules(ShipScheduleSearchCriteria criteria, Pageable pageable) {
    log.info("Fetching ship schedules with criteria: {}", criteria);
    return shipScheduleRepository.findAll(criteria, pageable);
  }

  @Override
  public List<ShipScheduleCrewAssignment> findAssignmentsOverlappingTimeRange(
      String profileId, Instant startDate, Instant endDate) {

    log.info(
        "Finding assignments overlapping time range for profileId: {}, startDate: {}, endDate:{}",
        profileId,
        startDate,
        endDate);

    List<ShipScheduleCrewAssignment> assignments =
        assignmentRepository.findByProfileIdAndTimeRangeOverlap(profileId, startDate, endDate);

    log.debug("Found {} overlapping assignments for profileId: {}", assignments.size(), profileId);

    return assignments;
  }

  @Override
  public List<ShipScheduleCrewAssignment> findAssignmentsFullyWithinTimeRange(
      String profileId, Instant startDate, Instant endDate) {

    log.info(
        "Finding assignments fully within time range for profileId: {}, startDate: {}, endDate: {}",
        profileId,
        startDate,
        endDate);

    List<ShipScheduleCrewAssignment> assignments =
        assignmentRepository.findByProfileIdFullyInTimeRange(profileId, startDate, endDate);

    log.debug(
        "Found {} assignments fully within time range for profileId: {}",
        assignments.size(),
        profileId);

    return assignments;
  }

  @Override
  public List<ShipScheduleCrewAssignment> findAssignmentsTimeRangeOverlap(
      Instant startDate, Instant endDate) {

    log.info(
        "Finding assignments between time range for startDate: {}, endDate: {}",
        startDate,
        endDate);

    return assignmentRepository.findByTimeRangeOverlap(startDate, endDate);
  }

  @Override
  public boolean hasAssignmentOverlap(String profileId, Instant startDate, Instant endDate) {

    log.info(
        "Checking for assignment overlap for profileId: {}, startDate: {}, endDate: {}",
        profileId,
        startDate,
        endDate);
    return assignmentRepository.existsProfileIdAndTimeRangeOverlap(profileId, startDate, endDate);
  }

  @Override
  public boolean hasAssignmentOverlapByEmployeeCardId(
      String employeeCardId, Instant startDate, Instant endDate) {

    log.info(
        "Checking for assignment overlap for employeeCardId: {}, startDate: {}, endDate: {}",
        employeeCardId,
        startDate,
        endDate);
    return assignmentRepository.existsEmployeeCardIdAndTimeRangeOverlap(
        employeeCardId, startDate, endDate);
  }

  @Override
  public List<ShipScheduleCrewAssignment> findAssignmentsByEmployeeCardIdsAndTimeRangeOverlap(
      Iterable<String> employeeCardIds, Instant startDate, Instant endDate) {

    log.info(
        "Finding assignments for employeeCardIds: {}, startDate: {}, endDate: {}",
        employeeCardIds,
        startDate,
        endDate);
    return assignmentRepository.findByEmployeeCardIdsAndTimeRangeOverlap(
        employeeCardIds, startDate, endDate);
  }
}

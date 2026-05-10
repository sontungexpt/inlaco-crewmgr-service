package com.inlaco.crewmgrservice.feature.shipschedule.application.service;

import com.inlaco.crewmgrservice.feature.crew.application.port.in.CrewUseCase;
import com.inlaco.crewmgrservice.feature.crew.domain.model.CrewProfile;
import com.inlaco.crewmgrservice.feature.shipschedule.application.port.in.ShipScheduleUseCase;
import com.inlaco.crewmgrservice.feature.shipschedule.application.port.out.ShipScheduleRepository;
import com.inlaco.crewmgrservice.feature.shipschedule.domain.model.ShipSchedule;
import com.inlaco.crewmgrservice.feature.shipschedule.domain.model.ShipScheduleCrewAssignment;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShipScheduleService implements ShipScheduleUseCase {

  private final ShipScheduleRepository shipScheduleRepository;
  private final CrewUseCase crewUseCase;

  @Override
  public ShipSchedule createSchedule(
      ShipSchedule schedule, List<ShipScheduleCrewAssignment> assignments) {
    List<String> employeeCardIds =
        assignments.stream().map(ShipScheduleCrewAssignment::getEmployeeCardId).toList();

    List<CrewProfile> crewProfiles = crewUseCase.getProfilesByEmployeeCardIds(employeeCardIds);

    if (crewProfiles.size() != employeeCardIds.size()) {
      throw new IllegalArgumentException("Some crew members do not exist");
    }

    Map<String, CrewProfile> crewProfileMap =
        crewProfiles.stream()
            .collect(Collectors.toMap(CrewProfile::getEmployeeCardId, Function.identity()));

    enrichAssignments(assignments, crewProfileMap);

    ShipSchedule created = shipScheduleRepository.save(schedule);
    assignments.forEach(assignment -> assignment.setScheduleId(created.getId()));

    return created;
  }

  private void enrichSchedule(
      ShipSchedule schedule,
      List<ShipScheduleCrewAssignment> assignments,
      String shipImageAssetId) {}

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
}

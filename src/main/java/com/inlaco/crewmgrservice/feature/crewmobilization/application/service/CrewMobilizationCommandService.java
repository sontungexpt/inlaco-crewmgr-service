package com.inlaco.crewmgrservice.feature.crewmobilization.application.service;

import com.inlaco.crewmgrservice.feature.crew.application.port.in.CrewUseCase;
import com.inlaco.crewmgrservice.feature.crew.domain.model.CrewProfile;
import com.inlaco.crewmgrservice.feature.crewmobilization.application.port.in.CrewMobilizationCommandUseCase;
import com.inlaco.crewmgrservice.feature.crewmobilization.application.port.out.CrewMobilizationRepository;
import com.inlaco.crewmgrservice.feature.crewmobilization.domain.event.NewCrewMobilizationEvent;
import com.inlaco.crewmgrservice.feature.crewmobilization.domain.model.AssignedCrew;
import com.inlaco.crewmgrservice.feature.crewmobilization.domain.model.CrewMobilizationSchedule;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CrewMobilizationCommandService implements CrewMobilizationCommandUseCase {

  private final CrewMobilizationRepository crewMobilizationScheduleRepository;
  private final CrewUseCase crewUseCase;
  private final ApplicationEventPublisher eventPublisher;

  @Override
  @Transactional
  public CrewMobilizationSchedule createSchedule(CrewMobilizationSchedule schedule) {
    log.debug("Starting schedule creation with details: {}", schedule);

    List<String> employeeCardIds =
        schedule.getCrews().stream().map(AssignedCrew::getEmployeeCardId).toList();
    List<CrewProfile> profiles = crewUseCase.getProfilesByEmployeeCardIds(employeeCardIds);

    if (profiles.size() != employeeCardIds.size()) {
      log.warn("Schedule creation failed: Some crew members do not exist");
      throw new IllegalArgumentException("Some crew members do not exist");
    }

    Map<String, CrewProfile> profileMap =
        profiles.stream()
            .collect(Collectors.toMap(CrewProfile::getEmployeeCardId, Function.identity()));

    schedule
        .getCrews()
        .forEach(
            crew -> {
              CrewProfile profile = profileMap.get(crew.getEmployeeCardId());
              if (profile != null) {
                crew.setProfileId(profile.getId());
                crew.setAccountId(profile.getAccountId());
              }
            });

    log.debug("Saving schedule to repository");
    var newSchedule = crewMobilizationScheduleRepository.save(schedule);

    log.info(
        "Schedule created successfully [id={}, startDate={}, endDate={}]",
        newSchedule.getId(),
        newSchedule.getStartDate(),
        newSchedule.getEndDate());

    log.debug(
        "Publishing NewCrewMobilizationScheduleEvent for schedule ID: {}", newSchedule.getId());
    eventPublisher.publishEvent(new NewCrewMobilizationEvent(newSchedule));

    return newSchedule;
  }
}

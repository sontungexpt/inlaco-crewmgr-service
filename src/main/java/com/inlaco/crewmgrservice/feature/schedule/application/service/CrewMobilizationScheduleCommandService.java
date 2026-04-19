package com.inlaco.crewmgrservice.feature.schedule.application.service;

import com.inlaco.crewmgrservice.feature.crew.application.port.in.CrewUseCase;
import com.inlaco.crewmgrservice.feature.schedule.application.port.in.CrewMobilizationScheduleCommandUseCase;
import com.inlaco.crewmgrservice.feature.schedule.application.port.out.CrewMobilizationScheduleRepository;
import com.inlaco.crewmgrservice.feature.schedule.domain.event.NewCrewMobilizationScheduleEvent;
import com.inlaco.crewmgrservice.feature.schedule.domain.model.AssignedCrew;
import com.inlaco.crewmgrservice.feature.schedule.domain.model.CrewMobilizationSchedule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CrewMobilizationScheduleCommandService
    implements CrewMobilizationScheduleCommandUseCase {

  private final CrewMobilizationScheduleRepository crewMobilizationScheduleRepository;
  private final CrewUseCase crewUseCase;
  private final ApplicationEventPublisher eventPublisher;

  @Override
  @Transactional
  public CrewMobilizationSchedule createSchedule(CrewMobilizationSchedule schedule) {
    log.debug("Starting schedule creation with details: {}", schedule);

    if (!crewUseCase.existsAllByEmployeeCardIds(
        schedule.getCrews().stream().map(AssignedCrew::getEmployeeCardId).toList())) {
      log.warn("Schedule creation failed: Some crew members do not exist");
      throw new IllegalArgumentException("Some crew members do not exist");
    }

    log.debug("Saving schedule to repository");
    var newSchedule = crewMobilizationScheduleRepository.save(schedule);

    log.info(
        "Schedule created successfully [id={}, startDate={}, endDate={}]",
        newSchedule.getId(),
        newSchedule.getStartDate(),
        newSchedule.getEndDate());

    log.debug(
        "Publishing NewCrewMobilizationScheduleEvent for schedule ID: {}", newSchedule.getId());
    eventPublisher.publishEvent(new NewCrewMobilizationScheduleEvent(newSchedule));

    return newSchedule;
  }
}

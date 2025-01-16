package com.inlaco.crewmgrservice.feature.schedule.service.impl;

import com.inlaco.crewmgrservice.feature.schedule.model.AssigmentSchedule;
import com.inlaco.crewmgrservice.feature.schedule.repository.AssignmentScheduleRepository;
import com.inlaco.crewmgrservice.feature.schedule.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScheduleServiceImpl implements ScheduleService {

  private final AssignmentScheduleRepository scheduleRepository;

  @Override
  public AssigmentSchedule createSchedule(AssigmentSchedule schedule) {
    return scheduleRepository.save(schedule);
  }
}

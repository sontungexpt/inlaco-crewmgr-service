package com.inlaco.crewmgrservice.feature.schedule.service.impl;

import com.inlaco.crewmgrservice.feature.schedule.model.Schedule;
import com.inlaco.crewmgrservice.feature.schedule.repository.ScheduleRepository;
import com.inlaco.crewmgrservice.feature.schedule.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScheduleServiceImpl implements ScheduleService {

  private final ScheduleRepository scheduleRepository;

  @Override
  public Schedule createSchedule(Schedule schedule) {
    return scheduleRepository.save(schedule);
  }
}

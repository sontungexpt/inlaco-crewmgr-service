package com.inlaco.crewmgrservice.feature.schedule.service.impl;

import com.inlaco.crewmgrservice.feature.schedule.dto.SailorWorkScheduleResponse;
import com.inlaco.crewmgrservice.feature.schedule.model.MasterAssignmentSchedule;
import com.inlaco.crewmgrservice.feature.schedule.service.ScheduleService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScheduleServiceImpl implements ScheduleService {
  @Override
  public Page<SailorWorkScheduleResponse> getScheduleBySailorId(
      String sailorId, Pageable pageable) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getScheduleBySailorId'");
  }

  @Override
  public MasterAssignmentSchedule createSchedule(
      MasterAssignmentSchedule masterAssignmentSchedule, List<String> sailorIds) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'createSchedule'");
  }
}

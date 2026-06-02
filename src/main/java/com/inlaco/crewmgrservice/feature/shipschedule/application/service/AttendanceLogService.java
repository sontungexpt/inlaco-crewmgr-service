package com.inlaco.crewmgrservice.feature.shipschedule.application.service;

import com.inlaco.crewmgrservice.feature.shipschedule.application.model.AttendanceLogSearchCriteria;
import com.inlaco.crewmgrservice.feature.shipschedule.application.port.in.AttendanceLogUseCase;
import com.inlaco.crewmgrservice.feature.shipschedule.application.port.out.AttendanceLogRepository;
import com.inlaco.crewmgrservice.feature.shipschedule.domain.model.AttendanceLog;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AttendanceLogService implements AttendanceLogUseCase {

  private final AttendanceLogRepository attendanceLogRepository;

  @Override
  public Page<AttendanceLog> getLogs(AttendanceLogSearchCriteria criteria, Pageable pageable) {
    return attendanceLogRepository.findAll(criteria, pageable);
  }
}

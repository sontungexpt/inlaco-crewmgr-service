package com.inlaco.crewmgrservice.feature.attendance.presentation.mapper;

import com.inlaco.crewmgrservice.feature.attendance.domain.model.AttendanceLog;
import com.inlaco.crewmgrservice.feature.attendance.presentation.dto.response.AttendanceResponse;
import org.springframework.stereotype.Component;

@Component
public class AttendanceMapper {

  public AttendanceResponse toAttendanceResponse(AttendanceLog log) {
    return AttendanceResponse.builder()
        .id(log.getId())
        .scheduleId(log.getScheduleId())
        .personId(log.getPersonId())
        .timestamp(log.getTimestamp())
        .build();
  }
}

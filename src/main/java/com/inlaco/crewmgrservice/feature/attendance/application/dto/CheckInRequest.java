package com.inlaco.crewmgrservice.feature.attendance.application.dto;

import com.inlaco.crewmgrservice.feature.attendance.domain.enums.AttendanceMethod;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CheckInRequest {
  private String scheduleId;
  private AttendanceMethod method;
  private String location;
}

package com.inlaco.crewmgrservice.feature.attendance.presentation.dto.response;

import java.time.Instant;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AttendanceResponse {

  private String id;
  private String scheduleId;
  private String personId;
  private Instant timestamp;
}

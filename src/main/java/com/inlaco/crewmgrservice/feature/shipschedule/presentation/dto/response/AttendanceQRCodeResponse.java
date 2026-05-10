package com.inlaco.crewmgrservice.feature.shipschedule.presentation.dto.response;

import com.inlaco.crewmgrservice.feature.shipschedule.domain.enums.CheckType;
import java.time.Instant;
import lombok.Data;

@Data
public class AttendanceQRCodeResponse {

  private String token;
  private String shipScheduleId;
  private String employeeCardId;
  private CheckType type;
  private Instant expiresAt;
  private Instant createdAt;
}

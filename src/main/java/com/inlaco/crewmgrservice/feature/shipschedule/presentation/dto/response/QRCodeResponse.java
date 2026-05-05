package com.inlaco.crewmgrservice.feature.shipschedule.presentation.dto.response;

import com.inlaco.crewmgrservice.feature.shipschedule.domain.enums.QRType;
import java.time.Instant;
import lombok.Data;

@Data
public class QRCodeResponse {
  
  private String id;
  private String token;
  private String shipScheduleId;
  private String employeeCardId;
  private QRType type;
  private Instant expiresAt;
  private Instant createdAt;
}

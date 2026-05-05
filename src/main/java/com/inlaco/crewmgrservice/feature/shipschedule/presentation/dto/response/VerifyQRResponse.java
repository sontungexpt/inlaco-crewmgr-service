package com.inlaco.crewmgrservice.feature.shipschedule.presentation.dto.response;

import com.inlaco.crewmgrservice.feature.shipschedule.domain.enums.QRType;
import java.time.Instant;
import lombok.Data;

@Data
public class VerifyQRResponse {
  
  private boolean valid;
  private String message;
  private String shipScheduleId;
  private String employeeCardId;
  private QRType type;
  private Instant usedAt;
  private String deviceId;
  private String location;
}

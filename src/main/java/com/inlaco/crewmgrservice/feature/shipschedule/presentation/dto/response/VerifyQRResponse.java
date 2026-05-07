package com.inlaco.crewmgrservice.feature.shipschedule.presentation.dto.response;

import com.inlaco.crewmgrservice.feature.shipschedule.domain.enums.CheckType;
import java.time.Instant;
import lombok.Data;

@Data
public class VerifyQRResponse {

  private boolean valid;
  private String message;
  private String shipScheduleId;
  private String employeeCardId;
  private CheckType type;
  private Instant usedAt;
  private String deviceId;
  private String location;
}

package com.inlaco.crewmgrservice.feature.totp.presentation.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TotpResponse {

  private String message;
  private boolean verified;
  private boolean exists;
  private boolean enabled;
  private int verificationCount;
}

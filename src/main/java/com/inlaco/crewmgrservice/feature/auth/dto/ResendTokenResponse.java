package com.inlaco.crewmgrservice.feature.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ResendTokenResponse {

  @Schema(description = "The resend token")
  private String resendToken;

  private Instant issuedDate;

  private Instant resendLockExpiryDate;
}

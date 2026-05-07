package com.inlaco.crewmgrservice.feature.shipschedule.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class VerifyQRRequest {

  @NotBlank(message = "QR token is required")
  private String token;

  @NotBlank(message = "Device ID is required")
  private String deviceId;

  private String location;
}

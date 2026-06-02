package com.inlaco.crewmgrservice.feature.shipschedule.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class VerifyQRAttendanceRequest extends VerifyAttendanceRequest {

  @NotBlank(message = "QR token is required")
  private String token;

  @NotBlank(message = "Device ID is required")
  private String deviceId;
}

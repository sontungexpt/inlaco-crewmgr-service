package com.inlaco.crewmgrservice.feature.attendance.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CheckInRequest {

  @NotBlank private String qrToken;

  @NotBlank private String deviceId;

  private String location;
}

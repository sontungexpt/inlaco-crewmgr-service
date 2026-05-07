package com.inlaco.crewmgrservice.feature.apikey.presentation.dto.request;

import com.inlaco.crewmgrservice.feature.apikey.domain.model.ApiKeyType;
import com.inlaco.crewmgrservice.feature.otp.domain.model.Otp;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateApiKeyWithOtpRequest {

  @NotBlank(message = "Client name is required")
  private String clientName;

  @NotBlank(message = "Description is required")
  private String description;

  private ApiKeyType type;

  // TOTP verification fields
  private String totpCode;
  private String totpPurposeId;

  // Normal OTP verification fields
  private String otpCode;
  private String otpPurposeId;
  private Otp.OtpSenderType otpSenderType;

  public boolean isTotpVerification() {
    return totpCode != null && !totpCode.trim().isEmpty();
  }

  public boolean isNormalOtpVerification() {
    return otpCode != null && !otpCode.trim().isEmpty();
  }
}

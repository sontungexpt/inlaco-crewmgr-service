package com.inlaco.crewmgrservice.feature.totp.presentation.dto.request;

import com.inlaco.crewmgrservice.feature.totp.domain.model.TotpSecret;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VerifyTotpRequest {
  
  @NotBlank(message = "TOTP code is required")
  @Size(min = 6, max = 6, message = "TOTP code must be 6 digits")
  private String code;
  
  @NotNull(message = "Purpose is required")
  private TotpSecret.TotpPurpose purpose;
  
  @NotBlank(message = "Purpose ID is required")
  private String purposeId;
}

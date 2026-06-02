package com.inlaco.crewmgrservice.feature.totp.presentation.dto.request;

import com.inlaco.crewmgrservice.feature.totp.domain.model.TotpSecret;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SetupTotpRequest {
  
  @NotNull(message = "Purpose is required")
  private TotpSecret.TotpPurpose purpose;
  
  @NotBlank(message = "Purpose ID is required")
  private String purposeId;
}

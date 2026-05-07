package com.inlaco.crewmgrservice.feature.apikey.presentation.dto.request;

import com.inlaco.crewmgrservice.feature.apikey.domain.model.ApiKeyType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateApiKeyWithTotpRequest {

  @NotBlank(message = "Client name is required")
  @Size(max = 100, message = "Client name must not exceed 100 characters")
  private String clientName;

  @Size(max = 500, message = "Description must not exceed 500 characters")
  private String description;

  private ApiKeyType type;

  @NotBlank(message = "TOTP code is required")
  @Size(min = 6, max = 6, message = "TOTP code must be 6 digits")
  private String totpCode;

  @NotBlank(message = "Purpose ID is required")
  private String purposeId;
}

package com.inlaco.crewmgrservice.feature.apikey.presentation.dto.response;

import com.inlaco.crewmgrservice.feature.apikey.domain.model.ApiKeyType;
import java.time.Instant;
import lombok.Data;

@Data
public class ApiKeyResponse {

  private String id;
  private String keyId;
  private String keySecret;
  private String clientName;
  private String description;
  private boolean active;
  private Instant createdAt;
  private Instant expiresAt;
  private String createdBy;
  private ApiKeyType type;
}

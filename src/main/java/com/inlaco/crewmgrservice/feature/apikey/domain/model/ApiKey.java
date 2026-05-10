package com.inlaco.crewmgrservice.feature.apikey.domain.model;

import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiKey {
  private String id;
  private String keyId;
  private String keySecret;
  private String clientName;
  private String description;
  private boolean active;
  private Instant createdAt;
  private Instant expiresAt;
  private ApiKeyType type;
  private String createdBy;

  public boolean isValid() {
    return active && !isExpired();
  }

  public boolean isExpired() {
    return expiresAt != null && expiresAt.isBefore(Instant.now());
  }

  public void deactivate() {
    this.active = false;
  }

  public void activate() {
    this.active = true;
  }
}

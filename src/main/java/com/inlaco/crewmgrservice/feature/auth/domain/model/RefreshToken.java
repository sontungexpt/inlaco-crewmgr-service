package com.inlaco.crewmgrservice.feature.auth.domain.model;

import java.time.Instant;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = "id")
public class RefreshToken {

  private String id;

  private String hashedToken;

  private String userPubId;

  private Instant expiresAt;

  private Instant revokedAt;

  private Instant createdAt;

  private Instant updatedAt;

  public RefreshToken(String hashedToken, String userPubId, Instant expiresAt) {
    this.userPubId = userPubId;
    this.expiresAt = expiresAt;
    this.hashedToken = hashedToken;
  }

  public boolean isExpired() {
    return Instant.now().isAfter(expiresAt);
  }

  public boolean isActive() {
    return !isExpired() && revokedAt == null;
  }

  public boolean isRevoked() {
    return revokedAt != null;
  }

  public void revoke() {
    if (isRevoked()) return;
    revokedAt = Instant.now();
  }
}

package com.inlaco.crewmgrservice.feature.auth.domain.model;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import java.time.Instant;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = "id")
public class RefreshToken {

  private String id;

  private String userId;

  private String token;

  private Instant expiresAt;

  private Instant revokedAt;

  private Instant createdAt;

  private Instant updatedAt;

  public RefreshToken(String userId, Instant expiresAt) {
    this.userId = userId;
    this.expiresAt = expiresAt;
    this.token = NanoIdUtils.randomNanoId();
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

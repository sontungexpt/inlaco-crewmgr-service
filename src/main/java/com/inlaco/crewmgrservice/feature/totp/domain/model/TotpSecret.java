package com.inlaco.crewmgrservice.feature.totp.domain.model;

import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TotpSecret {

  private String id;
  private String userId;
  private String secret;
  private String email;
  private TotpPurpose purpose;
  private String purposeId;
  private boolean enabled;
  private Instant createdAt;
  private Instant lastUsedAt;
  private int verificationCount;

  public static TotpSecret generateNew(
      String userId, String email, String secret, TotpPurpose purpose, String purposeId) {

    return TotpSecret.builder()
        .userId(userId)
        .email(email)
        .secret(secret)
        .purpose(purpose)
        .purposeId(purposeId)
        .enabled(true)
        .createdAt(Instant.now())
        .verificationCount(0)
        .build();
  }

  public void markAsUsed() {
    this.lastUsedAt = Instant.now();
    this.verificationCount++;
  }

  public void disable() {
    this.enabled = false;
  }

  public enum TotpPurpose {
    API_KEY_CREATION,
    SECRET_KEY_VIEWING,
    PASSWORD_RESET,
    ACCOUNT_VERIFICATION
  }
}

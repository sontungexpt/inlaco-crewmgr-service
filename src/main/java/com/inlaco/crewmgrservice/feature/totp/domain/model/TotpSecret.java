package com.inlaco.crewmgrservice.feature.totp.domain.model;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import dev.samstevens.totp.secret.SecretGenerator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TotpSecret {
  private String id;
  private String userId;
  private String email;
  private String secret;
  private TotpPurpose purpose;
  private String purposeId;
  private boolean enabled;
  private Instant createdAt;
  private Instant lastUsedAt;
  private int verificationCount;

  public static TotpSecret generateNew(String userId, String email, TotpPurpose purpose, String purposeId) {
    SecretGenerator generator = new SecretGenerator();
    String secret = generator.generate();
    
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

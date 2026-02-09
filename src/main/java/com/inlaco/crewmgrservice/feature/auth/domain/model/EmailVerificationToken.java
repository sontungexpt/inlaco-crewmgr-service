package com.inlaco.crewmgrservice.feature.auth.domain.model;

import java.time.Instant;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailVerificationToken extends VerificationToken {

  public static final long EXPIRE_SECONDS = 30 * 60;
  public static final long RESEND_LOCK_SECONDS = 60;

  private String hashToken;

  private Long ttl;

  private Instant lastSentAt;

  public void refresh(String newHashToken) {
    this.hashToken = newHashToken;
    this.ttl = EXPIRE_SECONDS; // reset TTL
    this.lastSentAt = Instant.now();
  }

  public EmailVerificationToken(String userId, String hashToken) {
    super(userId);
    refresh(hashToken);
  }

  public boolean canResend() {
    return lastSentAt == null || Instant.now().isAfter(lastSentAt.plusSeconds(RESEND_LOCK_SECONDS));
  }
}

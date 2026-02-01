package com.inlaco.crewmgrservice.feature.auth.model;

import java.time.Instant;
import java.util.concurrent.TimeUnit;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

@Getter
@Setter
@RedisHash(value = "email_verification_token")
public class EmailVerificationToken extends VerificationToken {

  public static final long EXPIRE_SECONDS = 30 * 60;
  public static final long RESEND_LOCK_SECONDS = 60;

  @Indexed private String hashToken;

  @TimeToLive(unit = TimeUnit.SECONDS)
  private Long ttl;

  private Instant lastSentAt;

  public void refresh(String newHashToken) {
    this.hashToken = newHashToken;
    this.ttl = EXPIRE_SECONDS; // reset TTL
    this.lastSentAt = Instant.now();
  }

  protected EmailVerificationToken() {
    super(null);
  }

  public EmailVerificationToken(String userId, String hashToken) {
    super(userId);
    refresh(hashToken);
  }

  public String getHashToken() {
    return hashToken;
  }

  public boolean canResend() {
    return lastSentAt == null || Instant.now().isAfter(lastSentAt.plusSeconds(RESEND_LOCK_SECONDS));
  }
}

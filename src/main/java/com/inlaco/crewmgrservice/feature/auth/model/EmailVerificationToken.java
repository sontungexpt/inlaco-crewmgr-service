package com.inlaco.crewmgrservice.feature.auth.model;

import java.time.Instant;
import java.util.concurrent.TimeUnit;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

@Getter
@Setter
@SuperBuilder
// @Document(collection = "email_verification_token")
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

  public EmailVerificationToken(String userId, String hashToken) {
    super(userId);
    this.hashToken = hashToken;
    this.ttl = EXPIRE_SECONDS;
    this.lastSentAt = Instant.now();
  }

  public String getHashToken() {
    return hashToken;
  }

  public boolean canResend() {
    return lastSentAt == null || Instant.now().isAfter(lastSentAt.plusSeconds(RESEND_LOCK_SECONDS));
  }
}

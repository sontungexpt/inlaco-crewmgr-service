package com.inlaco.crewmgrservice.feature.auth.infrastructure.persistence.redis.entity;

import java.time.Instant;
import java.util.concurrent.TimeUnit;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

@Data
@RedisHash(value = "email_verification_token")
public class EmailVerificationTokenEntity {

  @Id private String id;

  private String userId;

  private Instant createdAt;

  @Indexed private String hashToken;

  @TimeToLive(unit = TimeUnit.SECONDS)
  private Long ttl;

  private Instant lastSentAt;
}

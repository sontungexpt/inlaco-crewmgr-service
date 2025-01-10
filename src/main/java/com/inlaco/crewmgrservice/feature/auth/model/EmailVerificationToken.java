package com.inlaco.crewmgrservice.feature.auth.model;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import java.time.Instant;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import lombok.Builder.Default;
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

  public static final long EXPIRATION_TIME_IN_S = 24 * 60 * 60; // 1 day

  @Indexed private String token;

  @Default
  @TimeToLive(unit = TimeUnit.SECONDS)
  private long expiryTimeInS = EXPIRATION_TIME_IN_S;

  public EmailVerificationToken() {
    super();
  }

  public Instant getExpiryDate() {
    return getIssuedDate().plusSeconds(EXPIRATION_TIME_IN_S);
  }

  public EmailVerificationToken(String userId) {
    super(userId);
    generateToken();
  }

  public String getToken() {
    return token;
  }

  @Override
  public boolean isValid(String token) {
    return this.token.equals(token);
  }

  private String generateToken() {
    token = NanoIdUtils.randomNanoId();
    return token;
  }

  public Instant getResendLockExpiryDate() {
    return getIssuedDate().plusSeconds(60);
  }

  public boolean canResend() {
    return Instant.now().isAfter(getResendLockExpiryDate());
  }

  @Override
  public VerificationToken refresh(boolean newToken) {
    if (newToken) {
      return new EmailVerificationToken(getUserId());
    }
    generateToken();
    setIssuedDate(Instant.now());
    return this;
  }

  /**
   * Convert the object to a map representation This is useful for storing the object in a Redis
   * database
   *
   * @return a map representation of the object
   */
  public Map<String, String> toMap() {
    Map<String, String> map = super.toMap();
    map.put("token", token);
    return map;
  }

  /**
   * Convert a map representation of the object to an object
   *
   * @param map the map representation of the object
   * @return the object
   */
  public static EmailVerificationToken fromMap(Map<String, String> map) {
    return EmailVerificationToken.builder()
        .userId(map.get("userId"))
        .token(map.get("token"))
        .issuedDate(Instant.parse(map.get("issuedDate")))
        .build();
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), token);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    else if (obj instanceof EmailVerificationToken that) {
      return super.equals(that) || Objects.equals(token, that.token);
    }
    return false;
  }
}

package com.inlaco.crewmgrservice.feature.auth.model;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import java.time.Instant;
import java.util.Map;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.redis.core.RedisHash;

@Getter
@SuperBuilder
@RedisHash(timeToLive = EmailVerificationToken.EXPIRATION_TIME_IN_S)
public class EmailVerificationToken extends VerificationToken {

  public static final long EXPIRATION_TIME_IN_S = 24 * 60 * 60; // 1 day

  private String token;

  public Instant getExpiryDate() {
    return issuedDate.plusSeconds(EXPIRATION_TIME_IN_S);
  }

  public EmailVerificationToken(String userPubId) {
    super(userPubId);
    generateToken();
  }

  public String getToken() {
    return token;
  }

  public boolean isValid(String token) {
    return this.token.equals(token);
  }

  private String generateToken() {
    token = NanoIdUtils.randomNanoId();
    return token;
  }

  public Instant getResendLockExpiryDate() {
    return issuedDate.plusSeconds(60);
  }

  public boolean canResend() {
    return Instant.now().isAfter(getResendLockExpiryDate());
  }

  @Override
  public VerificationToken refresh(boolean newToken) {
    if (newToken) {
      return new EmailVerificationToken(userId);
    }
    generateToken();
    issuedDate = Instant.now();
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
}

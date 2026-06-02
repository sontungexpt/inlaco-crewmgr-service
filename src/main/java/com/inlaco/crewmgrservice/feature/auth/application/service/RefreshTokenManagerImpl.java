package com.inlaco.crewmgrservice.feature.auth.application.service;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.inlaco.crewmgrservice.feature.auth.application.model.result.RefreshRotationResult;
import com.inlaco.crewmgrservice.feature.auth.application.port.in.RefreshTokenManager;
import com.inlaco.crewmgrservice.feature.auth.application.port.out.RefreshTokenRepository;
import com.inlaco.crewmgrservice.feature.auth.domain.error.AuthErrorCode;
import com.inlaco.crewmgrservice.feature.auth.domain.exception.RefreshTokenException;
import com.inlaco.crewmgrservice.feature.auth.domain.model.RefreshToken;
import com.inlaco.crewmgrservice.shared.crypto.DigestUtils;
import com.mongodb.DuplicateKeyException;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class RefreshTokenManagerImpl implements RefreshTokenManager {

  private static short MAX_RETRY = 5;

  @Value("${token.refresh-token.expiration}")
  private long REFRESH_TOKEN_EXPIRATION;

  private final RefreshTokenRepository repository;

  @Override
  public String issue(String userPubId) {
    for (int attempt = 0; attempt < MAX_RETRY; attempt++) {
      String rawToken = generateRaw();
      String hash = hash(rawToken);
      try {
        repository.save(
            new RefreshToken(hash, userPubId, Instant.now().plusMillis(REFRESH_TOKEN_EXPIRATION)));
        log.info("Refresh token issued successfully for user {}", userPubId);
        return rawToken;
      } catch (DuplicateKeyException e) {
        log.debug("Refresh token collision detected. Retrying... attempt={}", attempt + 1);
      }
    }
    log.error("Unable to generate unique refresh token after 5 attempts");
    throw new IllegalStateException("Unable to generate unique refresh token after 5 attempts");
  }

  @Override
  public RefreshRotationResult rotate(String rawToken) {
    String hash = hash(rawToken);
    RefreshToken current = repository.findByHashedToken(hash).orElseThrow(this::notFoundException);

    validate(current);

    String userPubId = current.getUserPubId();

    current.revoke();
    log.info("Refresh token revoked for user {}", userPubId);
    repository.save(current);

    String newRaw = issue(userPubId);

    return new RefreshRotationResult(userPubId, newRaw);
  }

  @Override
  public void revoke(String rawToken) {
    String hash = hash(rawToken);
    RefreshToken token = repository.findByHashedToken(hash).orElseThrow(this::notFoundException);
    token.revoke();
    repository.save(token);
  }

  public void validate(RefreshToken refreshToken) {
    if (refreshToken.isRevoked()) {
      throw new RefreshTokenException(
          AuthErrorCode.AUTH_REFRESH_TOKEN_REVOKED, "Refresh token revoked");
    } else if (refreshToken.isExpired()) {
      throw new RefreshTokenException(
          AuthErrorCode.AUTH_REFRESH_TOKEN_EXPIRED, "Refresh token expired");
    }
  }

  private RefreshTokenException notFoundException() {
    return new RefreshTokenException(
        AuthErrorCode.AUTH_REFRESH_TOKEN_NOT_FOUND, "Refresh token not found");
  }

  private String generateRaw() {
    return NanoIdUtils.randomNanoId();
  }

  private String hash(String raw) {
    return DigestUtils.sha256(raw);
  }
}

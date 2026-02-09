package com.inlaco.crewmgrservice.feature.auth.application.service;

import com.inlaco.crewmgrservice.feature.auth.application.port.in.RefreshTokenManager;
import com.inlaco.crewmgrservice.feature.auth.application.port.out.RefreshTokenRepository;
import com.inlaco.crewmgrservice.feature.auth.domain.exception.RefreshTokenException;
import com.inlaco.crewmgrservice.feature.auth.domain.model.RefreshToken;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class RefreshTokenManagerImpl implements RefreshTokenManager {

  @Value("${auth.refresh-token.expiration}")
  private long refreshTokenExpiration;

  private final RefreshTokenRepository refreshTokenRepository;

  @Override
  public RefreshToken generate(String subject) {
    return new RefreshToken(subject, Instant.now().plusMillis(refreshTokenExpiration));
  }

  @Override
  public void validate(RefreshToken refreshToken) {
    if (refreshToken.isRevoked()) {
      throw new RefreshTokenException(refreshToken, "Refresh token revoked");
    } else if (refreshToken.isExpired()) {
      throw new RefreshTokenException(refreshToken, "Refresh token expired");
    }
  }

  @Override
  public RefreshToken findByToken(String token) {
    return refreshTokenRepository
        .findByToken(token)
        .orElseThrow(
            () -> {
              log.warn("Refresh token {} not found", token);
              return new RefreshTokenException(null, "Refresh token not found");
            });
  }

  @Override
  public RefreshToken save(RefreshToken refreshToken) {
    return refreshTokenRepository.save(refreshToken);
  }
}

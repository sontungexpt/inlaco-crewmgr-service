package com.inlaco.crewmgrservice.feature.auth.service.impl;

import com.inlaco.crewmgrservice.feature.auth.dto.JwtResponse;
import com.inlaco.crewmgrservice.feature.auth.model.RefreshToken;
import com.inlaco.crewmgrservice.feature.auth.repository.RefreshTokenRepository;
import com.inlaco.crewmgrservice.feature.auth.service.RefreshTokenService;
import com.inlaco.crewmgrservice.feature.auth.service.TokenService;
import com.inlaco.crewmgrservice.infrastructure.security.jwt.exception.JwtTokenException;
import java.util.function.Consumer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

  private final RefreshTokenRepository refreshTokenRepository;
  private final TokenService tokenService;

  @Override
  public RefreshToken validateRefreshToken(RefreshToken refreshToken) {
    return validateRefreshToken(refreshToken, (rt) -> {});
  }

  @Override
  public RefreshToken validateRefreshToken(
      RefreshToken refreshToken, Consumer<RefreshToken> instrusionHandler) {
    if (refreshToken.isRevoked()) {
      instrusionHandler.accept(refreshToken);
      throw new JwtTokenException(refreshToken.getToken(), "Refresh token revoked");
    } else if (refreshToken.isExpired()) {
      refreshToken.revoke(refreshTokenRepository);
      throw new JwtTokenException(refreshToken.getToken(), "Refresh token expired");
    }
    return refreshToken;
  }

  @Override
  public RefreshToken getRefreshToken(String token) {
    return refreshTokenRepository
        .findByToken(token)
        .orElseThrow(
            () -> {
              log.warn("Refresh token {} not found", token);
              return new JwtTokenException(token, "Refresh token not found");
            });
  }

  @Override
  public RefreshToken getAndValidateRefreshToken(String token) {
    return validateRefreshToken(getRefreshToken(token));
  }

  @Override
  public RefreshToken getAndValidateRefreshToken(
      String token, Consumer<RefreshToken> instrusionHandler) {
    return validateRefreshToken(getRefreshToken(token), instrusionHandler);
  }

  @Override
  @Transactional
  public JwtResponse refreshJwtTokens(RefreshToken refreshToken) {
    String userPubId = refreshToken.getUserPubId();
    String newAccessToken = tokenService.generateAccessToken(userPubId);

    RefreshToken newRefreshToken = refreshToken.refresh(refreshTokenRepository);

    log.info(
        "Refresh token {} refreshed successfully for user with public id {}",
        refreshToken,
        refreshToken.getUserPubId());

    return new JwtResponse(newAccessToken, newRefreshToken.getToken());
  }

  @Override
  public RefreshToken generateRefreshToken(String userPubId) {
    return tokenService.generateRefreshToken(userPubId);
  }
}

package com.inlaco.crewmgrservice.feature.auth.application.service;

import com.inlaco.crewmgrservice.feature.auth.application.model.result.AuthTokenResult;
import com.inlaco.crewmgrservice.feature.auth.application.port.in.AccessTokenGenerator;
import com.inlaco.crewmgrservice.feature.auth.application.port.in.RefreshTokenManager;
import com.inlaco.crewmgrservice.feature.auth.application.port.in.RefreshTokenUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class RefreshTokenService implements RefreshTokenUseCase {

  private final RefreshTokenManager refreshTokenManager;
  private final AccessTokenGenerator accessTokenGenerator;

  @Override
  @Transactional
  public AuthTokenResult refresh(String refreshToken) {
    log.debug("Refreshing token for user {}", refreshToken);
    var result = refreshTokenManager.rotate(refreshToken);

    // Create new token
    final String newAccessToken = accessTokenGenerator.generate(result.userPubId());
    final String newRefreshToken = result.newRefreshToken();

    log.debug("Refresh token rotated for user {}", result.userPubId());
    return new AuthTokenResult(newAccessToken, newRefreshToken);
  }
}

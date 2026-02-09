package com.inlaco.crewmgrservice.feature.auth.application.service;

import com.inlaco.crewmgrservice.feature.auth.application.model.result.AuthTokenResult;
import com.inlaco.crewmgrservice.feature.auth.application.port.in.AccessTokenGenerator;
import com.inlaco.crewmgrservice.feature.auth.application.port.in.RefreshTokenManager;
import com.inlaco.crewmgrservice.feature.auth.application.port.in.RefreshTokenUseCase;
import com.inlaco.crewmgrservice.feature.auth.domain.model.RefreshToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RefreshTokenService implements RefreshTokenUseCase {

  private final RefreshTokenManager refreshTokenManager;
  private final AccessTokenGenerator accessTokenGenerator;

  @Override
  public AuthTokenResult refresh(String refreshToken) {
    RefreshToken current = refreshTokenManager.findByToken(refreshToken);
    refreshTokenManager.validate(current);

    String userId = current.getUserId();
    String newAccessToken = accessTokenGenerator.generate(userId);

    refreshTokenManager.save(current); // mark revorked

    // Create new token
    RefreshToken next = refreshTokenManager.generate(userId);
    refreshTokenManager.save(next);

    log.info("Refresh token rotated for user {}", userId);

    return new AuthTokenResult(newAccessToken, current.getToken());
  }
}

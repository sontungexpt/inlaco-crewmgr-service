package com.inlaco.crewmgrservice.feature.auth.application.service;

import com.inlaco.crewmgrservice.feature.auth.application.port.in.LogoutUseCase;
import com.inlaco.crewmgrservice.feature.auth.application.port.in.RefreshTokenManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutUseCase {

  private final RefreshTokenManager refreshTokenManager;

  @Override
  @Transactional
  public void logout(String refreshToken) {
    log.info("Attempting to log out with refresh token: {}", refreshToken);
    try {
      refreshTokenManager.revoke(refreshToken);
      log.info("Successfully logged out with refresh token: {}", refreshToken);
    } catch (Exception e) {
      log.error("Failed to log out with refresh token: {}", refreshToken, e);
      throw e;
    }
  }
}

package com.inlaco.crewmgrservice.feature.auth.application.service;

import com.inlaco.crewmgrservice.feature.auth.application.port.in.LogoutUseCase;
import com.inlaco.crewmgrservice.feature.auth.application.port.in.RefreshTokenManager;
import com.inlaco.crewmgrservice.feature.auth.domain.model.RefreshToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutUseCase {

  private final RefreshTokenManager refreshTokenManager;

  @Override
  @Transactional
  public void logout(String refreshToken) {
    RefreshToken token = refreshTokenManager.findByToken(refreshToken);
    token.revoke();
    refreshTokenManager.save(token);
  }
}

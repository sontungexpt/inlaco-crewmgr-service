package com.inlaco.crewmgrservice.feature.auth.application.port.in;

import com.inlaco.crewmgrservice.feature.auth.domain.model.RefreshToken;

public interface RefreshTokenManager {

  void validate(RefreshToken refreshToken);

  RefreshToken generate(String subject);

  RefreshToken findByToken(String token);

  RefreshToken save(RefreshToken refreshToken);
}

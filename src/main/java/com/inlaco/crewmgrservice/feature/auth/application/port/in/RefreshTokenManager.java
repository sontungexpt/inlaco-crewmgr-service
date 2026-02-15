package com.inlaco.crewmgrservice.feature.auth.application.port.in;

import com.inlaco.crewmgrservice.feature.auth.application.model.result.RefreshRotationResult;

public interface RefreshTokenManager {
  String issue(String userPubId);

  RefreshRotationResult rotate(String rawToken);

  void revoke(String rawToken);

  // void validate(RefreshToken refreshToken);

  // String hashToken(String token);

  // RefreshToken generate(String hashedToken, String subject);

  // String generateToken(String subject);

  // RefreshToken findByToken(String token);

  // RefreshToken save(RefreshToken refreshToken);

  // void deleteByToken(String token);
}

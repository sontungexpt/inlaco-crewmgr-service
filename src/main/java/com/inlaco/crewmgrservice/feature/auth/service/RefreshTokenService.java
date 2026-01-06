package com.inlaco.crewmgrservice.feature.auth.service;

import com.inlaco.crewmgrservice.feature.auth.dto.JwtResponse;
import com.inlaco.crewmgrservice.feature.auth.model.RefreshToken;
import java.util.function.Consumer;

/** RefreshTokenService */
public interface RefreshTokenService {

  RefreshToken validateRefreshToken(RefreshToken refreshToken);

  RefreshToken validateRefreshToken(
      RefreshToken refreshToken, Consumer<RefreshToken> instrusionHandler);

  RefreshToken getRefreshToken(String token);

  RefreshToken getAndValidateRefreshToken(String token);

  RefreshToken getAndValidateRefreshToken(String token, Consumer<RefreshToken> instrusionHandler);

  JwtResponse refreshJwtTokens(RefreshToken refreshToken);

  RefreshToken generateRefreshToken(String userPubId);
}

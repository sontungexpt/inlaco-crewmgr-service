package com.inlaco.crewmgrservice.feature.auth.application.port.out;

import com.inlaco.crewmgrservice.feature.auth.domain.model.RefreshToken;
import java.util.Optional;

public interface RefreshTokenRepository {

  RefreshToken save(RefreshToken refreshToken);

  Optional<RefreshToken> findByHashedToken(String token);
}

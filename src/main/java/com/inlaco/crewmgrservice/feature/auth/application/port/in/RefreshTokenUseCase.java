package com.inlaco.crewmgrservice.feature.auth.application.port.in;

import com.inlaco.crewmgrservice.feature.auth.application.model.result.AuthTokenResult;

public interface RefreshTokenUseCase {

  AuthTokenResult refresh(String refreshToken);
}

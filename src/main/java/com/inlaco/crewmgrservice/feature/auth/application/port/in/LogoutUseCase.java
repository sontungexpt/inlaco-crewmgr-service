package com.inlaco.crewmgrservice.feature.auth.application.port.in;

public interface LogoutUseCase {
  void logout(String refreshToken);
}

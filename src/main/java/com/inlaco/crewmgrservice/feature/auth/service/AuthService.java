package com.inlaco.crewmgrservice.feature.auth.service;

import com.inlaco.crewmgrservice.feature.auth.dto.JwtResponse;
import com.inlaco.crewmgrservice.feature.auth.dto.LoginRequest;
import com.inlaco.crewmgrservice.feature.auth.dto.LoginResponse;
import com.inlaco.crewmgrservice.feature.auth.dto.RegistrationRequest;

public interface AuthService {

  LoginResponse login(LoginRequest loginRequest);

  void register(RegistrationRequest request);

  JwtResponse refreshToken(String refreshToken);

  void resend2StepVerification(String identifier);

  void verify2StepVerifiction(String token);
}

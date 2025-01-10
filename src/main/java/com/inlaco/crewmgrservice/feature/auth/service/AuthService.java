package com.inlaco.crewmgrservice.feature.auth.service;

import com.inlaco.crewmgrservice.feature.auth.dto.JwtResponse;
import com.inlaco.crewmgrservice.feature.auth.dto.LoginRequest;
import com.inlaco.crewmgrservice.feature.auth.dto.LoginResponse;
import com.inlaco.crewmgrservice.feature.auth.dto.RegistrationRequest;
import com.inlaco.crewmgrservice.feature.auth.dto.ResendTokenResponse;

public interface AuthService {

  LoginResponse login(LoginRequest loginRequest);

  ResendTokenResponse register(RegistrationRequest request);

  JwtResponse refreshToken(String refreshToken);

  ResendTokenResponse resend2StepVerification(String identifier);

  void verify2StepVerifiction(String token);
}

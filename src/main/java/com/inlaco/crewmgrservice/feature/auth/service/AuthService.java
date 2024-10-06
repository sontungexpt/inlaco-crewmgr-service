package com.inlaco.crewmgrservice.feature.auth.service;

import com.inlaco.crewmgrservice.feature.auth.dto.JwtResponse;
import com.inlaco.crewmgrservice.feature.auth.dto.LoginRequest;
import com.inlaco.crewmgrservice.feature.auth.dto.LoginResponse;
import com.inlaco.crewmgrservice.feature.auth.dto.RegistrationRequest;
import org.springframework.web.bind.annotation.RequestHeader;

public interface AuthService {

  LoginResponse login(LoginRequest loginRequest);

  void register(RegistrationRequest request);

  JwtResponse refreshToken(@RequestHeader("Authorization") String refreshToken);
}

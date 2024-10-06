package com.inlaco.crewmgrservice.feature.auth.controller;

import com.inlaco.crewmgrservice.annotation.BearerToken;
import com.inlaco.crewmgrservice.annotation.PublicEndpoint;
import com.inlaco.crewmgrservice.feature.auth.dto.LoginRequest;
import com.inlaco.crewmgrservice.feature.auth.dto.RegistrationRequest;
import com.inlaco.crewmgrservice.feature.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.ServletException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@PublicEndpoint
public record AuthController(AuthService authService) {

  @Operation(summary = "Registers a new user to the system")
  @PostMapping("/register")
  @ResponseStatus(HttpStatus.ACCEPTED)
  public void registerUser(@RequestBody @Valid RegistrationRequest signUpRequest) {
    authService.register(signUpRequest);
  }

  @Operation(summary = "Logs the user in to the system and return the auth tokens")
  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody @Valid LoginRequest request) {
    return ResponseEntity.ok(authService.login(request));
  }

  @PostMapping("/refresh-token")
  @Operation(summary = "Refresh the expired jwt authentication")
  public ResponseEntity<?> refreshToken(@BearerToken String refreshToken) throws ServletException {
    return ResponseEntity.ok(authService.refreshToken(refreshToken));
  }
}

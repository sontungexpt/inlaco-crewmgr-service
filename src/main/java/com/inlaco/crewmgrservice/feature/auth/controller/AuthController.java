package com.inlaco.crewmgrservice.feature.auth.controller;

import com.inlaco.crewmgrservice.annotation.BearerToken;
import com.inlaco.crewmgrservice.annotation.PublicEndpoint;
import com.inlaco.crewmgrservice.config.OpenApiConfig;
import com.inlaco.crewmgrservice.feature.auth.dto.LoginRequest;
import com.inlaco.crewmgrservice.feature.auth.dto.RegistrationRequest;
import com.inlaco.crewmgrservice.feature.auth.service.AuthService;
import com.inlaco.crewmgrservice.feature.auth.service.RefreshTokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Authentication", description = "A collection of authentication endpoints")
public record AuthController(RefreshTokenService refreshTokenService, AuthService authService) {

  @Operation(
      summary = "Registers a new user to the system",
      description =
          """
Register new user to the system.

**Usecase**:
- UC_account-dang-ky

""")
  @PostMapping("/register")
  @ResponseStatus(HttpStatus.ACCEPTED)
  public void registerUser(@RequestBody @Valid RegistrationRequest signUpRequest) {
    authService.register(signUpRequest);
  }

  @Operation(
      summary = "Logs the user in to the system and return the auth tokens",
      description =
          """
Login to the existed account

**Usecase**:
- UC_account-dang-nhap

""")
  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody @Valid LoginRequest request) {
    return ResponseEntity.ok(authService.login(request));
  }

  @PostMapping("/refresh-token")
  @Operation(
      summary = "Refresh the expired jwt authentication",
      description =
          """
Retrive a new access token and refresh token

**Usecase**:
- UC_account-dang-nhap

""",
      security = @SecurityRequirement(name = OpenApiConfig.BEARER_AUTH_NAME))
  public ResponseEntity<?> refreshToken(@BearerToken String refreshToken) throws ServletException {
    return ResponseEntity.ok(authService.refreshToken(refreshToken));
  }
}

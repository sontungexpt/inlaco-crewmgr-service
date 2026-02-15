package com.inlaco.crewmgrservice.feature.auth.presentation.rest.controller;

import com.inlaco.crewmgrservice.feature.auth.application.model.command.LoginCommand;
import com.inlaco.crewmgrservice.feature.auth.application.model.command.RegisterCommand;
import com.inlaco.crewmgrservice.feature.auth.application.port.in.LoginUseCase;
import com.inlaco.crewmgrservice.feature.auth.application.port.in.LogoutUseCase;
import com.inlaco.crewmgrservice.feature.auth.application.port.in.RefreshTokenUseCase;
import com.inlaco.crewmgrservice.feature.auth.application.port.in.RegistrationUseCase;
import com.inlaco.crewmgrservice.feature.auth.presentation.dto.request.LoginRequest;
import com.inlaco.crewmgrservice.feature.auth.presentation.dto.request.RegistrationRequest;
import com.inlaco.crewmgrservice.feature.auth.presentation.dto.response.AuthTokenResponse;
import com.inlaco.crewmgrservice.feature.auth.presentation.mapper.AuthTokenResponseMapper;
import com.inlaco.crewmgrservice.infrastructure.config.openapi.OpenApiConfig;
import com.inlaco.crewmgrservice.infrastructure.web.annotation.BearerToken;
import com.inlaco.crewmgrservice.infrastructure.web.annotation.PublicEndpoint;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@PublicEndpoint
@Tag(name = "Authentication", description = "A collection of authentication endpoints")
public record AuthController(
    RegistrationUseCase registrationUseCase,
    AuthTokenResponseMapper authTokenResultMapper,
    RefreshTokenUseCase refreshTokenUseCase,
    LogoutUseCase logoutUseCase,
    LoginUseCase loginUseCase) {

  @Operation(summary = "Registers a new user to the system")
  @PostMapping("/register")
  @ResponseStatus(HttpStatus.ACCEPTED)
  public AuthTokenResponse register(@RequestBody @Valid RegistrationRequest signUpRequest) {
    return authTokenResultMapper.toDTO(
        registrationUseCase.register(
            new RegisterCommand(
                signUpRequest.username(),
                signUpRequest.password(),
                signUpRequest.confirmPassword(),
                signUpRequest.name())));
  }

  @Operation(summary = "Logs the user in to the system and return the auth tokens")
  @PostMapping("/login")
  public AuthTokenResponse login(@RequestBody @Valid LoginRequest request) {
    return authTokenResultMapper.toDTO(
        loginUseCase.login(new LoginCommand(request.username(), request.password())));
  }

  @Operation(summary = "Logs the user out of the system")
  @PostMapping("/logout")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void logout(@BearerToken String refreshToken) {
    logoutUseCase.logout(refreshToken);
  }

  @PostMapping("/refresh-token")
  @Operation(
      summary = "Refresh the expired jwt authentication",
      security = @SecurityRequirement(name = OpenApiConfig.BEARER_AUTH_NAME))
  public AuthTokenResponse refreshToken(@BearerToken String refreshToken) {
    return authTokenResultMapper.toDTO(refreshTokenUseCase.refresh(refreshToken));
  }
}

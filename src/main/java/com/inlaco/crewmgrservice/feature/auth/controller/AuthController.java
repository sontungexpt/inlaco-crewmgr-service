package com.inlaco.crewmgrservice.feature.auth.controller;

import com.inlaco.crewmgrservice.annotation.BearerToken;
import com.inlaco.crewmgrservice.annotation.PublicEndpoint;
import com.inlaco.crewmgrservice.config.OpenApiConfig;
import com.inlaco.crewmgrservice.endpoint.APIEndpointMap;
import com.inlaco.crewmgrservice.endpoint.APIEndpointName;
import com.inlaco.crewmgrservice.feature.auth.dto.LoginRequest;
import com.inlaco.crewmgrservice.feature.auth.dto.RegistrationRequest;
import com.inlaco.crewmgrservice.feature.auth.service.AuthService;
import com.inlaco.crewmgrservice.feature.auth.service.RefreshTokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.ServletException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
  @APIEndpointMap(
      name = APIEndpointName.AUTH_REGISTER,
      displayName = "Register new user",
      description = "Register new user to the system")
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
  @APIEndpointMap(
      name = APIEndpointName.AUTH_LOGIN,
      displayName = "Login to the system",
      description = "Login to the existed account")
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
  @APIEndpointMap(
      name = APIEndpointName.AUTH_REFRESH_TOKEN,
      displayName = "Refresh the expired jwt authentication",
      description = "Retrive a new access token and refresh token")
  public ResponseEntity<?> refreshToken(@BearerToken String refreshToken) throws ServletException {
    return ResponseEntity.ok(authService.refreshToken(refreshToken));
  }

  @Operation(
      summary = "Verify the two step verification",
      responses = {
        @ApiResponse(responseCode = "204", description = "Verify successfully"),
        @ApiResponse(responseCode = "400", description = "Request invalid"),
        @ApiResponse(responseCode = "404", description = "Token expired"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
      },
      description =
          """
Verify the two step verification

**Usecase**:
- UC_account-dang-ky

""")
  @GetMapping("/two-step-verification")
  @APIEndpointMap(
      name = APIEndpointName.AUTH_REGISTER,
      displayName = "Verify the two step verification",
      description = "Verify the two step verification")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void verifyTwoStepVerification(@RequestParam("token") String token) {
    authService.verify2StepVerifiction(token);
  }

  @Operation(
      summary = "Resend the two step verification",
      responses = {
        @ApiResponse(responseCode = "204", description = "Resend successfully"),
        @ApiResponse(responseCode = "400", description = "Bad request"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
      },
      description =
          """
Resend the two step verification

**Usecase**:
- UC_account-dang-ky

""")
  @PostMapping("/two-step-verification/resend")
  @APIEndpointMap(
      name = APIEndpointName.AUTH_REGISTER,
      displayName = "Resend the two step verification",
      description = "Resend the two step verification")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void resendTwoStepVerification(@RequestParam("username") String username) {
    authService.resend2StepVerification(username);
  }
}

package com.inlaco.crewmgrservice.feature.totp.presentation.rest.controller;

import com.inlaco.crewmgrservice.feature.totp.application.port.in.TotpUseCase;
import com.inlaco.crewmgrservice.feature.totp.presentation.dto.request.SetupTotpRequest;
import com.inlaco.crewmgrservice.feature.totp.presentation.dto.request.VerifyTotpRequest;
import com.inlaco.crewmgrservice.feature.totp.presentation.dto.response.TotpResponse;
import com.inlaco.crewmgrservice.feature.totp.presentation.dto.response.TotpSetupResponse;
import com.inlaco.crewmgrservice.feature.user.domain.model.User;
import com.inlaco.crewmgrservice.infrastructure.config.openapi.OpenApiConfig;
import com.inlaco.crewmgrservice.infrastructure.web.annotation.CurrentUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/totp")
@Tag(name = "TOTP Management", description = "APIs for Time-based One-Time Password operations")
@Slf4j
public class TotpController {

  private final TotpUseCase totpUseCase;

  @PostMapping("/setup")
  @Operation(
      summary = "Setup TOTP for specific purpose",
      security = {@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH_NAME)})
  @RolesAllowed("USER")
  public ResponseEntity<TotpSetupResponse> setupTotp(
      @Valid @RequestBody SetupTotpRequest request, @CurrentUser User user) {

    String userId = user.getId();
    String email = user.getUsername();

    TotpSetupResponse response =
        totpUseCase.setupTotp(userId, email, request.getPurpose(), request.getPurposeId());

    return ResponseEntity.ok(response);
  }

  @PostMapping("/verify")
  @Operation(
      summary = "Verify TOTP code",
      security = {@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH_NAME)})
  public ResponseEntity<TotpResponse> verifyTotp(
      @Valid @RequestBody VerifyTotpRequest request, @CurrentUser User user) {

    String userId = user.getId();
    boolean isValid =
        totpUseCase.verifyTotp(
            userId, request.getCode(), request.getPurpose(), request.getPurposeId());

    TotpResponse response =
        TotpResponse.builder()
            .message(isValid ? "TOTP verified successfully" : "Invalid TOTP code")
            .verified(isValid)
            .build();

    if (isValid) {
      return ResponseEntity.ok(response);
    } else {
      return ResponseEntity.badRequest().body(response);
    }
  }
}

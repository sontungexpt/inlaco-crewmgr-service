package com.inlaco.crewmgrservice.feature.totp.presentation.rest.controller;

import com.inlaco.crewmgrservice.feature.totp.application.port.in.TotpUseCase;
import com.inlaco.crewmgrservice.feature.totp.domain.model.TotpSecret;
import com.inlaco.crewmgrservice.feature.totp.domain.exception.TotpException;
import com.inlaco.crewmgrservice.feature.totp.presentation.dto.request.SetupTotpRequest;
import com.inlaco.crewmgrservice.feature.totp.presentation.dto.request.VerifyTotpRequest;
import com.inlaco.crewmgrservice.feature.totp.presentation.dto.response.TotpResponse;
import com.inlaco.crewmgrservice.feature.totp.presentation.dto.response.TotpSetupResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/totp")
@Tag(name = "TOTP Management", description = "APIs for Time-based One-Time Password operations")
@Slf4j
public class TotpController {

  private final TotpUseCase totpUseCase;

  @PostMapping("/setup")
  @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
  @Operation(summary = "Setup TOTP for specific purpose")
  public ResponseEntity<TotpSetupResponse> setupTotp(
      @Valid @RequestBody SetupTotpRequest request,
      Authentication authentication) {
    
    String userId = authentication.getName();
    String email = authentication.getName(); // Assuming username is email
    
    TotpSetupResponse response = totpUseCase.setupTotp(userId, email, request.getPurpose(), request.getPurposeId());
    
    return ResponseEntity.ok(response);
  }

  @PostMapping("/verify")
  @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
  @Operation(summary = "Verify TOTP code")
  public ResponseEntity<TotpResponse> verifyTotp(
      @Valid @RequestBody VerifyTotpRequest request,
      Authentication authentication) {
    
    String userId = authentication.getName();
    boolean isValid = totpUseCase.verifyTotp(userId, request.getCode(), request.getPurpose(), request.getPurposeId());
    
    if (isValid) {
      return ResponseEntity.ok(TotpResponse.builder()
          .message("TOTP verified successfully")
          .verified(true)
          .build());
    } else {
      return ResponseEntity.badRequest().body(TotpResponse.builder()
          .message("Invalid TOTP code")
          .verified(false)
          .build());
    }
  }

  @GetMapping("/status")
  @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
  @Operation(summary = "Check TOTP status")
  public ResponseEntity<TotpResponse> checkTotpStatus(
      @RequestParam TotpSecret.TotpPurpose purpose,
      @RequestParam String purposeId,
      Authentication authentication) {
    
    String userId = authentication.getName();
    TotpSecret secret = totpUseCase.getTotpSecret(userId, purpose, purposeId);
    
    if (secret == null) {
      return ResponseEntity.ok(TotpResponse.builder()
          .message("No TOTP found")
          .exists(false)
          .build());
    }
    
    return ResponseEntity.ok(TotpResponse.builder()
        .message("TOTP status retrieved")
        .exists(true)
        .enabled(secret.isEnabled())
        .verificationCount(secret.getVerificationCount())
        .build());
  }

  @DeleteMapping("/disable")
  @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
  @Operation(summary = "Disable TOTP")
  public ResponseEntity<TotpResponse> disableTotp(
      @RequestParam TotpSecret.TotpPurpose purpose,
      @RequestParam String purposeId,
      Authentication authentication) {
    
    String userId = authentication.getName();
    totpUseCase.disableTotp(userId, purpose, purposeId);
    
    return ResponseEntity.ok(TotpResponse.builder()
        .message("TOTP disabled successfully")
        .verified(true)
        .build());
  }

  @ExceptionHandler(TotpException.class)
  public ResponseEntity<TotpResponse> handleTotpException(TotpException ex) {
    return ResponseEntity.badRequest().body(TotpResponse.builder()
        .message(ex.getMessage())
        .verified(false)
        .build());
  }
}

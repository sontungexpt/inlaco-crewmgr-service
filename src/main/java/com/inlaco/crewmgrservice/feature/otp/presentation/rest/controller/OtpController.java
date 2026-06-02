package com.inlaco.crewmgrservice.feature.otp.presentation.rest.controller;

import com.inlaco.crewmgrservice.feature.otp.application.port.in.OtpUseCase;
import com.inlaco.crewmgrservice.feature.otp.application.service.OtpService;
import com.inlaco.crewmgrservice.feature.otp.domain.model.Otp;
import com.inlaco.crewmgrservice.feature.user.domain.model.User;
import com.inlaco.crewmgrservice.infrastructure.web.annotation.CurrentUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/otp")
@Tag(name = "OTP", description = "APIs for OTP verification via email or SMS")
public class OtpController {

  private final OtpUseCase otpUseCase;

  @PostMapping("/send")
  @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
  @Operation(summary = "Send OTP via email or SMS")
  public ResponseEntity<String> sendOtp(
      @RequestParam String recipient,
      @RequestParam Otp.OtpPurpose purpose,
      @RequestParam Otp.OtpSenderType senderType,
      @CurrentUser User user) {

    String userId = user.getId();
    String purposeId = OtpService.generatePurposeId(purpose);

    String otpId = otpUseCase.sendOtp(userId, recipient, purpose, purposeId, senderType);

    return ResponseEntity.ok(otpId);
  }

  @PostMapping("/verify")
  @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
  @Operation(summary = "Verify OTP")
  public ResponseEntity<Boolean> verifyOtp(
      @RequestParam String otpCode,
      @RequestParam Otp.OtpPurpose purpose,
      @RequestParam String purposeId,
      @CurrentUser User user) {

    String userId = user.getId();
    boolean isValid = otpUseCase.verifyOtp(userId, otpCode, purpose, purposeId);

    return ResponseEntity.ok(isValid);
  }
}

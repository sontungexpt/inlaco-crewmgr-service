package com.inlaco.crewmgrservice.feature.otp.application.service;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.inlaco.crewmgrservice.feature.notify.sender.NotificationDispatcher;
import com.inlaco.crewmgrservice.feature.notify.sender.email.EmailRequest;
import com.inlaco.crewmgrservice.feature.otp.application.port.in.OtpUseCase;
import com.inlaco.crewmgrservice.feature.otp.application.port.out.OtpRepository;
import com.inlaco.crewmgrservice.feature.otp.domain.exception.OtpExpiredException;
import com.inlaco.crewmgrservice.feature.otp.domain.exception.OtpInvalidException;
import com.inlaco.crewmgrservice.feature.otp.domain.exception.OtpNotFoundException;
import com.inlaco.crewmgrservice.feature.otp.domain.exception.OtpSendFailedException;
import com.inlaco.crewmgrservice.feature.otp.domain.model.Otp;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class OtpService implements OtpUseCase {

  private final OtpRepository otpRepository;
  private final NotificationDispatcher notificationDispatcher;

  @Override
  @Transactional
  public String sendOtp(
      String userId,
      String recipient,
      Otp.OtpPurpose purpose,
      String purposeId,
      Otp.OtpSenderType senderType) {
    try {
      // Delete any existing OTP for this purpose
      otpRepository.deleteByUserIdAndPurposeAndPurposeId(userId, purpose, purposeId);

      // Generate new OTP
      Otp otp = Otp.generateNew(userId, recipient, purpose, purposeId, senderType);
      otpRepository.save(otp);

      // Send OTP using notification dispatcher
      sendOtpNotification(otp);

      log.info("OTP sent via {} to {} for purpose: {}", senderType, recipient, purpose);
      return otp.getId();

    } catch (Exception e) {
      log.error("Failed to send OTP", e);
      throw new OtpSendFailedException("Failed to send OTP", e);
    }
  }

  @Override
  @Transactional
  public boolean verifyOtp(
      String userId, String otpCode, Otp.OtpPurpose purpose, String purposeId) {
    Otp otp =
        otpRepository
            .findByUserIdAndPurposeAndPurposeId(userId, purpose, purposeId)
            .orElseThrow(() -> new OtpNotFoundException("OTP not found for this request"));

    if (otp.isExpired()) {
      throw new OtpExpiredException("OTP has expired");
    }

    if (!otp.isValid(otpCode)) {
      throw new OtpInvalidException("Invalid OTP code");
    }

    otp.markAsUsed();
    otpRepository.save(otp);

    log.info("OTP verified successfully for userId={}, purpose={}", userId, purpose);
    return true;
  }

  @Override
  public Otp getOtp(String userId, Otp.OtpPurpose purpose, String purposeId) {
    return otpRepository
        .findByUserIdAndPurposeAndPurposeId(userId, purpose, purposeId)
        .orElse(null);
  }

  @Override
  @Transactional
  public void disableOtp(String userId, Otp.OtpPurpose purpose, String purposeId) {
    Otp otp =
        otpRepository
            .findByUserIdAndPurposeAndPurposeId(userId, purpose, purposeId)
            .orElseThrow(() -> new OtpNotFoundException("OTP not found for this request"));

    otp.markAsUsed();
    otpRepository.save(otp);
  }

  @Override
  @Transactional
  public void cleanupExpired() {
    otpRepository.cleanupExpired();
    log.info("Cleaned up expired OTPs");
  }

  private void sendOtpNotification(Otp otp) {
    switch (otp.getSenderType()) {
      case EMAIL -> {
        EmailRequest emailRequest =
            EmailRequest.builder(
                    com.inlaco.crewmgrservice.feature.notify.sender.email.EmailType.TEXT,
                    java.util.List.of(otp.getRecipient()),
                    getEmailBodyForPurpose(otp),
                    getSubjectForPurpose(otp.getPurpose()))
                .build();

        notificationDispatcher.sendNotification(emailRequest);
      }
      case SMS -> {}
      default ->
          throw new OtpSendFailedException("Unsupported sender type: " + otp.getSenderType());
    }
  }

  private String getSubjectForPurpose(Otp.OtpPurpose purpose) {
    return switch (purpose) {
      case API_KEY_CREATION -> "Your API Key Creation OTP";
      case SECRET_KEY_VIEWING -> "Your API Key Secret Viewing OTP";
      case PASSWORD_RESET -> "Your Password Reset OTP";
      case ACCOUNT_VERIFICATION -> "Your Account Verification OTP";
    };
  }

  private String getEmailBodyForPurpose(Otp otp) {
    return switch (otp.getPurpose()) {
      case API_KEY_CREATION ->
          "Your OTP for API key creation is: "
              + otp.getOtpCode()
              + "\n\n"
              + "This code will expire in 5 minutes. Please do not share this code with anyone.";
      case SECRET_KEY_VIEWING ->
          "Your OTP for viewing API key secret is: "
              + otp.getOtpCode()
              + "\n\n"
              + "This code will expire in 5 minutes. Please do not share this code with anyone.";
      case PASSWORD_RESET ->
          "Your OTP for password reset is: "
              + otp.getOtpCode()
              + "\n\n"
              + "This code will expire in 5 minutes. Please do not share this code with anyone.";
      case ACCOUNT_VERIFICATION ->
          "Your OTP for account verification is: "
              + otp.getOtpCode()
              + "\n\n"
              + "This code will expire in 5 minutes. Please do not share this code with anyone.";
    };
  }

  public static String generatePurposeId(Otp.OtpPurpose purpose) {
    return switch (purpose) {
      case API_KEY_CREATION ->
          "apikey-" + NanoIdUtils.randomNanoId() + "-" + System.currentTimeMillis();
      case SECRET_KEY_VIEWING ->
          "secret-" + NanoIdUtils.randomNanoId() + "-" + System.currentTimeMillis();
      case PASSWORD_RESET ->
          "password-" + NanoIdUtils.randomNanoId() + "-" + System.currentTimeMillis();
      case ACCOUNT_VERIFICATION ->
          "verify-" + NanoIdUtils.randomNanoId() + "-" + System.currentTimeMillis();
    };
  }
}

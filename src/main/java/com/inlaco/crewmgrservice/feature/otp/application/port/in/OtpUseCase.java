package com.inlaco.crewmgrservice.feature.otp.application.port.in;

import com.inlaco.crewmgrservice.feature.otp.domain.model.Otp;

public interface OtpUseCase {

  String sendOtp(
      String userId,
      String recipient,
      Otp.OtpPurpose purpose,
      String purposeId,
      Otp.OtpSenderType senderType);

  boolean verifyOtp(String userId, String otpCode, Otp.OtpPurpose purpose, String purposeId);

  Otp getOtp(String userId, Otp.OtpPurpose purpose, String purposeId);

  void disableOtp(String userId, Otp.OtpPurpose purpose, String purposeId);

  void cleanupExpired();
}

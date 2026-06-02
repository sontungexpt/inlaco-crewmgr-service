package com.inlaco.crewmgrservice.feature.otp.domain.model;

import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Otp {

  private String id;

  private String userId;

  private String recipient; // email or phone number

  private String otpCode;

  private OtpPurpose purpose;

  private String purposeId;

  private OtpSenderType senderType;

  private boolean isUsed = false;

  private Instant expiresAt;

  private Instant createdAt = Instant.now();

  public Otp() {}

  public Otp(
      String userId,
      String recipient,
      String otpCode,
      OtpPurpose purpose,
      String purposeId,
      OtpSenderType senderType,
      Instant expiresAt) {
    this.userId = userId;
    this.recipient = recipient;
    this.otpCode = otpCode;
    this.purpose = purpose;
    this.purposeId = purposeId;
    this.senderType = senderType;
    this.expiresAt = expiresAt;
  }

  public static Otp generateNew(
      String userId,
      String recipient,
      OtpPurpose purpose,
      String purposeId,
      OtpSenderType senderType) {
    String otpCode = String.format("%06d", (int) (Math.random() * 1000000));
    Instant expiresAt = Instant.now().plusSeconds(5 * 60); // 5 minutes expiry
    return new Otp(userId, recipient, otpCode, purpose, purposeId, senderType, expiresAt);
  }

  public boolean isExpired() {
    return Instant.now().isAfter(expiresAt);
  }

  public boolean isValid(String providedOtp) {
    return !isUsed() && !isExpired() && otpCode.equals(providedOtp);
  }

  public void markAsUsed() {
    this.isUsed = true;
  }

  public enum OtpPurpose {
    API_KEY_CREATION,
    SECRET_KEY_VIEWING,
    PASSWORD_RESET,
    ACCOUNT_VERIFICATION
  }

  public enum OtpSenderType {
    EMAIL,
    SMS
  }
}

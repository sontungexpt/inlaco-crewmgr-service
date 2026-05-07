package com.inlaco.crewmgrservice.feature.otp.domain.error;

import com.inlaco.crewmgrservice.shared.kernel.error.HttpMappableErrorCode;

public enum OtpErrorCode implements HttpMappableErrorCode {
  OTP_NOT_FOUND("OTP_NOT_FOUND", 404, "OTP not found for this request"),
  OTP_EXPIRED("OTP_EXPIRED", 400, "OTP has expired"),
  OTP_INVALID("OTP_INVALID", 400, "Invalid OTP code"),
  OTP_ALREADY_USED("OTP_ALREADY_USED", 400, "OTP has already been used"),
  OTP_SEND_FAILED("OTP_SEND_FAILED", 500, "Failed to send OTP"),
  OTP_RATE_LIMIT_EXCEEDED(
      "OTP_RATE_LIMIT_EXCEEDED", 429, "Too many OTP requests, please try again later"),
  OTP_PURPOSE_MISMATCH("OTP_PURPOSE_MISMATCH", 400, "OTP purpose mismatch"),
  OTP_USER_MISMATCH("OTP_USER_MISMATCH", 403, "OTP user mismatch"),
  UNSUPPORTED_SENDER_TYPE("UNSUPPORTED_SENDER_TYPE", 400, "Unsupported OTP sender type");

  private final String code;
  private final int httpStatus;
  private final String defaultMessage;

  OtpErrorCode(String code, int httpStatus, String defaultMessage) {
    this.code = code;
    this.httpStatus = httpStatus;
    this.defaultMessage = defaultMessage;
  }

  @Override
  public String code() {
    return code;
  }

  @Override
  public int httpStatus() {
    return httpStatus;
  }

  public String getDefaultMessage() {
    return defaultMessage;
  }
}

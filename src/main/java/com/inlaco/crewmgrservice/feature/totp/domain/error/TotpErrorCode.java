package com.inlaco.crewmgrservice.feature.totp.domain.error;

import com.inlaco.crewmgrservice.shared.kernel.error.ErrorCode;
import com.inlaco.crewmgrservice.shared.kernel.error.HttpMappableErrorCode;

public enum TotpErrorCode implements ErrorCode, HttpMappableErrorCode {
  
  TOTP_NOT_FOUND("TOTP_NOT_FOUND", 404, "TOTP configuration not found for this request"),
  TOTP_DISABLED("TOTP_DISABLED", 403, "TOTP is disabled for this purpose"),
  TOTP_INVALID_CODE("TOTP_INVALID_CODE", 400, "Invalid TOTP verification code"),
  TOTP_EXPIRED("TOTP_EXPIRED", 400, "TOTP code has expired"),
  TOTP_SETUP_FAILED("TOTP_SETUP_FAILED", 500, "Failed to setup TOTP configuration"),
  TOTP_QR_GENERATION_FAILED("TOTP_QR_GENERATION_FAILED", 500, "Failed to generate QR code"),
  TOTP_RATE_LIMIT_EXCEEDED("TOTP_RATE_LIMIT_EXCEEDED", 429, "Too many TOTP attempts, please try again later"),
  TOTP_PURPOSE_MISMATCH("TOTP_PURPOSE_MISMATCH", 400, "TOTP purpose mismatch"),
  TOTP_USER_MISMATCH("TOTP_USER_MISMATCH", 403, "TOTP user mismatch");
  
  private final String code;
  private final int httpStatus;
  private final String defaultMessage;
  
  TotpErrorCode(String code, int httpStatus, String defaultMessage) {
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

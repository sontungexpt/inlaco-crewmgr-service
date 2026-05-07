package com.inlaco.crewmgrservice.feature.otp.domain.exception;

import com.inlaco.crewmgrservice.feature.otp.domain.error.OtpErrorCode;
import com.inlaco.crewmgrservice.shared.kernel.exception.ApplicationException;

public class OtpNotFoundException extends ApplicationException {

  public OtpNotFoundException(String message) {
    super(OtpErrorCode.OTP_NOT_FOUND, message);
  }

  public OtpNotFoundException(String message, Object data) {
    super(OtpErrorCode.OTP_NOT_FOUND, message, data);
  }
}

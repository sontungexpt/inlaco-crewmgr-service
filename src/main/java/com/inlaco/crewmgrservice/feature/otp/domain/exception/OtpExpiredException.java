package com.inlaco.crewmgrservice.feature.otp.domain.exception;

import com.inlaco.crewmgrservice.feature.otp.domain.error.OtpErrorCode;
import com.inlaco.crewmgrservice.shared.kernel.exception.ApplicationException;

public class OtpExpiredException extends ApplicationException {

  public OtpExpiredException(String message) {
    super(OtpErrorCode.OTP_EXPIRED, message);
  }

  public OtpExpiredException(String message, Object data) {
    super(OtpErrorCode.OTP_EXPIRED, message, data);
  }
}

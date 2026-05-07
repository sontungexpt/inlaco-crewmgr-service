package com.inlaco.crewmgrservice.feature.otp.domain.exception;

import com.inlaco.crewmgrservice.feature.otp.domain.error.OtpErrorCode;
import com.inlaco.crewmgrservice.shared.kernel.exception.ApplicationException;

public class OtpInvalidException extends ApplicationException {

  public OtpInvalidException(String message) {
    super(OtpErrorCode.OTP_INVALID, message);
  }

  public OtpInvalidException(String message, Object data) {
    super(OtpErrorCode.OTP_INVALID, message, data);
  }
}

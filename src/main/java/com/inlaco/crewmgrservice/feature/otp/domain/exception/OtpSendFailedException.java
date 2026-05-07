package com.inlaco.crewmgrservice.feature.otp.domain.exception;

import com.inlaco.crewmgrservice.feature.otp.domain.error.OtpErrorCode;
import com.inlaco.crewmgrservice.shared.kernel.exception.ApplicationException;

public class OtpSendFailedException extends ApplicationException {

  public OtpSendFailedException(String message) {
    super(OtpErrorCode.OTP_SEND_FAILED, message);
  }

  public OtpSendFailedException(String message, Object data) {
    super(OtpErrorCode.OTP_SEND_FAILED, message, data);
  }
}

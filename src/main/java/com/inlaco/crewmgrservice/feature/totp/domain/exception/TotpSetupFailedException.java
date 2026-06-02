package com.inlaco.crewmgrservice.feature.totp.domain.exception;

import com.inlaco.crewmgrservice.feature.totp.domain.error.TotpErrorCode;
import com.inlaco.crewmgrservice.shared.kernel.exception.ApplicationException;

public class TotpSetupFailedException extends ApplicationException {
  
  public TotpSetupFailedException(String message) {
    super(TotpErrorCode.TOTP_SETUP_FAILED, message);
  }
  
  public TotpSetupFailedException(String message, Object data) {
    super(TotpErrorCode.TOTP_SETUP_FAILED, message, data);
  }
}

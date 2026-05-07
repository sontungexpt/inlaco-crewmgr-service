package com.inlaco.crewmgrservice.feature.totp.domain.exception;

import com.inlaco.crewmgrservice.feature.totp.domain.error.TotpErrorCode;
import com.inlaco.crewmgrservice.shared.kernel.exception.ApplicationException;

public class TotpInvalidCodeException extends ApplicationException {
  
  public TotpInvalidCodeException(String message) {
    super(TotpErrorCode.TOTP_INVALID_CODE, message);
  }
  
  public TotpInvalidCodeException(String message, Object data) {
    super(TotpErrorCode.TOTP_INVALID_CODE, message, data);
  }
}

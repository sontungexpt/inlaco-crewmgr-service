package com.inlaco.crewmgrservice.feature.totp.domain.exception;

import com.inlaco.crewmgrservice.feature.totp.domain.error.TotpErrorCode;
import com.inlaco.crewmgrservice.shared.kernel.exception.ApplicationException;

public class TotpExpiredException extends ApplicationException {
  
  public TotpExpiredException(String message) {
    super(TotpErrorCode.TOTP_EXPIRED, message);
  }
  
  public TotpExpiredException(String message, Object data) {
    super(TotpErrorCode.TOTP_EXPIRED, message, data);
  }
}

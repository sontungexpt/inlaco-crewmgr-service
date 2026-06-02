package com.inlaco.crewmgrservice.feature.totp.domain.exception;

import com.inlaco.crewmgrservice.feature.totp.domain.error.TotpErrorCode;
import com.inlaco.crewmgrservice.shared.kernel.exception.ApplicationException;

public class TotpDisabledException extends ApplicationException {
  
  public TotpDisabledException(String message) {
    super(TotpErrorCode.TOTP_DISABLED, message);
  }
  
  public TotpDisabledException(String message, Object data) {
    super(TotpErrorCode.TOTP_DISABLED, message, data);
  }
}

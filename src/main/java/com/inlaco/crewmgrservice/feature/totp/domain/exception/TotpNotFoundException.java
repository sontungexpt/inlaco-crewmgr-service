package com.inlaco.crewmgrservice.feature.totp.domain.exception;

import com.inlaco.crewmgrservice.feature.totp.domain.error.TotpErrorCode;
import com.inlaco.crewmgrservice.shared.kernel.exception.ApplicationException;

public class TotpNotFoundException extends ApplicationException {
  
  public TotpNotFoundException(String message) {
    super(TotpErrorCode.TOTP_NOT_FOUND, message);
  }
  
  public TotpNotFoundException(String message, Object data) {
    super(TotpErrorCode.TOTP_NOT_FOUND, message, data);
  }
}

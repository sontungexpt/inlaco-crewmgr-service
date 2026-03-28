package com.inlaco.crewmgrservice.feature.auth.domain.exception;

import com.inlaco.crewmgrservice.shared.kernel.error.ErrorCode;
import com.inlaco.crewmgrservice.shared.kernel.exception.ApplicationException;
import lombok.Getter;

@Getter
public class RefreshTokenException extends ApplicationException {

  public RefreshTokenException(ErrorCode errorCode, String msg) {
    super(errorCode, msg);
  }
}

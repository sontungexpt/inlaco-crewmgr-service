package com.inlaco.crewmgrservice.shared.kernel.exception;

import com.inlaco.crewmgrservice.shared.kernel.error.ErrorCode;
import lombok.Getter;

@Getter
public abstract class ApplicationException extends RuntimeException {

  private final ErrorCode errorCode;
  private final String errorCodeStr;

  protected ApplicationException(ErrorCode errorCode, String message) {
    super(message);
    this.errorCode = errorCode;
    this.errorCodeStr = null;
  }

  protected ApplicationException(ErrorCode errorCode, String message, Object data) {
    super(message);
    this.errorCode = errorCode;
    this.errorCodeStr = null;
  }

  protected ApplicationException(String errorCode, String message) {
    super(message);
    this.errorCodeStr = errorCode;
    this.errorCode = null;
  }

  public Object getData() {
    return null;
  }
}

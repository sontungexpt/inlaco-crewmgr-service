package com.inlaco.crewmgrservice.application.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class ApplicationException extends RuntimeException {

  private final HttpStatus status;
  private final String errorCode;

  protected ApplicationException(String errorCode, HttpStatus status, String message) {
    super(message);
    this.errorCode = errorCode;
    this.status = status;
  }

  // compate with old code
  protected ApplicationException(String errorCode, String message, HttpStatus status) {
    super(message);
    this.errorCode = errorCode;
    this.status = status;
  }
}

package com.inlaco.crewmgrservice.application.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class ApplicationExceptionException extends RuntimeException {

  private final HttpStatus status;
  private final String errorCode;

  protected ApplicationExceptionException(String errorCode, HttpStatus status, String message) {
    super(message);
    this.errorCode = errorCode;
    this.status = status;
  }

  // compate with old code
  protected ApplicationExceptionException(String errorCode, String message, HttpStatus status) {
    super(message);
    this.errorCode = errorCode;
    this.status = status;
  }
}

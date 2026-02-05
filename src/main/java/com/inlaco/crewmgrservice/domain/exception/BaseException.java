package com.inlaco.crewmgrservice.domain.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class BaseException extends RuntimeException {

  private final HttpStatus status;
  private final String errorCode;
  private final Object data;

  protected BaseException(String errorCode, String message, HttpStatus status, Object data) {
    super(message);
    this.errorCode = errorCode;
    this.status = status;
    this.data = data;
  }

  protected BaseException(String errorCode, String message, HttpStatus status) {
    this(errorCode, message, status, null);
  }
}

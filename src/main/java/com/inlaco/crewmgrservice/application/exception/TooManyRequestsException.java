package com.inlaco.crewmgrservice.application.exception;

import org.springframework.http.HttpStatus;

public class TooManyRequestsException extends ApplicationExceptionException {

  public TooManyRequestsException(String message) {
    super("TOO_MANY_REQUESTS", HttpStatus.TOO_MANY_REQUESTS, message);
  }
}

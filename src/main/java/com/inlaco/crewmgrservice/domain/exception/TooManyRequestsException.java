package com.inlaco.crewmgrservice.domain.exception;

import org.springframework.http.HttpStatus;

public class TooManyRequestsException extends BaseException {

  public TooManyRequestsException(String message) {
    super("TOO_MANY_REQUESTS", message, HttpStatus.TOO_MANY_REQUESTS);
  }
}

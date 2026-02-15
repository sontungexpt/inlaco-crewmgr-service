package com.inlaco.crewmgrservice.shared.kernel.exception;

public class TooManyRequestsException extends ApplicationException {

  public TooManyRequestsException(String message) {
    super("TOO_MANY_REQUESTS", message);
  }
}

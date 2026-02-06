package com.inlaco.crewmgrservice.feature.auth.exceptions;

import com.inlaco.crewmgrservice.application.exception.ApplicationExceptionException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

@Getter
@Slf4j
public class TwoStepVerificationException extends ApplicationExceptionException {

  public TwoStepVerificationException(String message) {
    super("TWO_STEP_VERIFICATION_ERROR", message, HttpStatus.NOT_FOUND);
  }
}

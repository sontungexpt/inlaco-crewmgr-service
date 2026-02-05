package com.inlaco.crewmgrservice.feature.auth.exceptions;

import com.inlaco.crewmgrservice.domain.exception.BaseException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

@Getter
@Slf4j
public class TwoStepVerificationException extends BaseException {

  public TwoStepVerificationException(String message) {
    super("TWO_STEP_VERIFICATION_ERROR", message, HttpStatus.NOT_FOUND);
  }
}

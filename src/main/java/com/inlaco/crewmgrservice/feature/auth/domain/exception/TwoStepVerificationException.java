package com.inlaco.crewmgrservice.feature.auth.domain.exception;

import com.inlaco.crewmgrservice.shared.kernel.exception.ApplicationException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public class TwoStepVerificationException extends ApplicationException {

  public TwoStepVerificationException(String message) {
    super("TWO_STEP_VERIFICATION_ERROR", message);
  }
}

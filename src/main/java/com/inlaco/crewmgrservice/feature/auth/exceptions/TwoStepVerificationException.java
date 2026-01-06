package com.inlaco.crewmgrservice.feature.auth.exceptions;

import com.inlaco.crewmgrservice.exceptions.BaseException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

@Getter
@Slf4j
public class TwoStepVerificationException extends BaseException {

  public TwoStepVerificationException(String message) {
    super(HttpStatus.NOT_FOUND, message);
  }
}

package com.inlaco.crewmgrservice.feature.contract.exception;

import com.inlaco.crewmgrservice.application.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public abstract class ContractException extends ApplicationException {

  public ContractException(HttpStatus status, String message) {

    super("CONTRACT_ERROR", message, status);
  }
}

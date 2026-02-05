package com.inlaco.crewmgrservice.feature.contract.exception;

import com.inlaco.crewmgrservice.domain.exception.BaseException;
import org.springframework.http.HttpStatus;

public abstract class ContractException extends BaseException {

  public ContractException(HttpStatus status, String message) {

    super("CONTRACT_ERROR", message, status);
  }
}

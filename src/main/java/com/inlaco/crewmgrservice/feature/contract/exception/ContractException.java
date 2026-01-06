package com.inlaco.crewmgrservice.feature.contract.exception;

import com.inlaco.crewmgrservice.exceptions.BaseException;
import org.springframework.http.HttpStatus;

public abstract class ContractException extends BaseException {

  public ContractException(HttpStatus status, String message) {
    super(status, message);
  }
}

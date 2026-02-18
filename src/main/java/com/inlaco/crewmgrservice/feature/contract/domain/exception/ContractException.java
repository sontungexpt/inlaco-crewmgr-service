package com.inlaco.crewmgrservice.feature.contract.domain.exception;

import com.inlaco.crewmgrservice.shared.kernel.error.ErrorCode;
import com.inlaco.crewmgrservice.shared.kernel.exception.ApplicationException;

public abstract class ContractException extends ApplicationException {

  public ContractException(ErrorCode errorCode, String message) {
    super(errorCode, message);
  }
}

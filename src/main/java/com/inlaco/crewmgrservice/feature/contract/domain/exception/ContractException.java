package com.inlaco.crewmgrservice.feature.contract.domain.exception;

import com.inlaco.crewmgrservice.shared.kernel.exception.ApplicationException;

public abstract class ContractException extends ApplicationException {

  public ContractException(String message) {
    super("CONTRACT_ERROR", message);
  }
}

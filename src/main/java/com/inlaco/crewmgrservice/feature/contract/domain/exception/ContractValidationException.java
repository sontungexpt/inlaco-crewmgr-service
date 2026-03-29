package com.inlaco.crewmgrservice.feature.contract.domain.exception;

import com.inlaco.crewmgrservice.feature.contract.domain.error.ContractErrorCode;

public class ContractValidationException extends ContractException {

  public ContractValidationException(String message, String field) {
    super(ContractErrorCode.CONTRACT_VALIDATION_FAILED, message, field);
  }
}

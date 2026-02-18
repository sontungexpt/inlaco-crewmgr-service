package com.inlaco.crewmgrservice.feature.contract.domain.exception;

import com.inlaco.crewmgrservice.feature.contract.domain.error.ContractErrorCode;

public class ContractFrozenException extends ContractException {

  public ContractFrozenException(String message) {
    super(ContractErrorCode.CONTRACT_FROZEN, message);
  }
}

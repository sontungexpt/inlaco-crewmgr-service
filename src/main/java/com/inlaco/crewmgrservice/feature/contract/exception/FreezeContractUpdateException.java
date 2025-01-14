package com.inlaco.crewmgrservice.feature.contract.exception;

import org.springframework.http.HttpStatus;

public class FreezeContractUpdateException extends ContractException {

  public FreezeContractUpdateException(String message) {
    super(HttpStatus.BAD_REQUEST, message);
  }

  public FreezeContractUpdateException(HttpStatus status, String message) {
    super(status, message);
  }
}

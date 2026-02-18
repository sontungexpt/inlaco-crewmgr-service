package com.inlaco.crewmgrservice.feature.contract.domain.error;

import com.inlaco.crewmgrservice.shared.kernel.error.ErrorCode;

public enum ContractErrorCode implements ErrorCode {
  CONTRACT_FROZEN("CONTRACT_ERR_001"),
  ;

  final String code;

  ContractErrorCode(String code) {
    this.code = code;
  }

  @Override
  public String code() {
    return name();
  }
}

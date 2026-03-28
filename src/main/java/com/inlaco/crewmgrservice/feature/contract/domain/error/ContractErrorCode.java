package com.inlaco.crewmgrservice.feature.contract.domain.error;

import com.inlaco.crewmgrservice.shared.kernel.error.HttpMappableErrorCode;

public enum ContractErrorCode implements HttpMappableErrorCode {
  CONTRACT_UNKNOWN_ERROR("CONTRACT_ERR_000", 500),
  CONTRACT_FROZEN("CONTRACT_ERR_001", 403),
  ;

  final String code;
  final int status;

  ContractErrorCode(String code, int status) {
    this.code = code;
    this.status = status;
  }

  @Override
  public String code() {
    return code;
  }

  @Override
  public int httpStatus() {
    return status;
  }
}

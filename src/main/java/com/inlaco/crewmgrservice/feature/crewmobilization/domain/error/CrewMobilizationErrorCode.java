package com.inlaco.crewmgrservice.feature.crewmobilization.domain.error;

import com.inlaco.crewmgrservice.shared.kernel.error.HttpMappableErrorCode;

public enum CrewMobilizationErrorCode implements HttpMappableErrorCode {
  CREW_MOBILIZATION_UNKNOWN_ERROR("CREW_MOBILIZATION_ERR_000", 500),
  CREW_MOBILIZATION_ASSIGNMENT_OVERLAP_ERROR("CREW_MOBILIZATION_ERR_001", 400);

  final String code;
  final int status;

  CrewMobilizationErrorCode(String code, int status) {
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

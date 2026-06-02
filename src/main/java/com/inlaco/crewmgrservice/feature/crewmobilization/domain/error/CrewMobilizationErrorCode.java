package com.inlaco.crewmgrservice.feature.crewmobilization.domain.error;

import com.inlaco.crewmgrservice.shared.kernel.error.HttpMappableErrorCode;

public enum CrewMobilizationErrorCode implements HttpMappableErrorCode {
  CREW_MOBILIZATION_UNKNOWN_ERROR("CREW_MOBILIZATION_ERR_000", 500),
  CREW_MOBILIZATION_ASSIGNMENT_OVERLAP_ERROR("CREW_MOBILIZATION_ERR_001", 400),
  CREW_MOBILIZATION_INVALID_ASSIGNMENTS("CREW_MOBILIZATION_ERR_002", 400),
  CREW_MOBILIZATION_INVALID_ASSIGNMENT_DATE("CREW_MOBILIZATION_ERR_003", 400),
  CREW_MOBILIZATION_CREW_NOT_FOUND("CREW_MOBILIZATION_ERR_004", 404),
  CREW_MOBILIZATION_CREW_HAS_NO_ACCOUNT("CREW_MOBILIZATION_ERR_005", 400),
  CREW_MOBILIZATION_CONTRACT_NOT_ACTIVE("CREW_MOBILIZATION_ERR_006", 400),
  CREW_MOBILIZATION_CONTRACT_NOT_FOUND("CREW_MOBILIZATION_ERR_007", 404);

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

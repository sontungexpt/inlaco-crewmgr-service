package com.inlaco.crewmgrservice.feature.shipschedule.domain.error;

import com.inlaco.crewmgrservice.shared.kernel.error.HttpMappableErrorCode;

public enum ShipScheduleErrorCode implements HttpMappableErrorCode {
  SHIP_SCHEDULE_UNKNOWN_ERROR("SHIP_SCHEDULE_ERR_000", 500),

  // Ship validation errors
  SHIP_IMO_NUMBER_REQUIRED("SHIP_SCHEDULE_ERR_001", 400),
  SHIP_NOT_FOUND_IN_ACTIVE_CONTRACTS("SHIP_SCHEDULE_ERR_002", 404),
  SHIP_NOT_AUTHORIZED_FOR_USER("SHIP_SCHEDULE_ERR_003", 403),

  // Crew assignment errors
  CREW_PROFILE_NOT_FOUND("SHIP_SCHEDULE_ERR_004", 404),
  CREW_MEMBER_HAS_NO_ACCOUNT("SHIP_SCHEDULE_ERR_005", 400);

  private final String code;
  private final int status;

  ShipScheduleErrorCode(String code, int status) {
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

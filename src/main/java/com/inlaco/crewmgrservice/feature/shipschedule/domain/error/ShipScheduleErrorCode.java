package com.inlaco.crewmgrservice.feature.shipschedule.domain.error;

import com.inlaco.crewmgrservice.shared.kernel.error.HttpMappableErrorCode;

public enum ShipScheduleErrorCode implements HttpMappableErrorCode {
  SHIP_SCHEDULE_UNKNOWN_ERROR("SHIP_SCHEDULE_ERR_000", 500),

  // ===================== Ship validation =====================
  SHIP_IMO_NUMBER_REQUIRED("SHIP_SCHEDULE_ERR_001", 400),
  SHIP_NOT_FOUND_IN_ACTIVE_CONTRACTS("SHIP_SCHEDULE_ERR_002", 404),
  SHIP_NOT_AUTHORIZED_FOR_USER("SHIP_SCHEDULE_ERR_003", 403),

  // ===================== Crew profile =====================
  CREW_PROFILE_NOT_FOUND("SHIP_SCHEDULE_ERR_004", 404),
  CREW_MEMBER_HAS_NO_ACCOUNT("SHIP_SCHEDULE_ERR_005", 400),

  // ===================== Assignment validation =====================
  INVALID_ASSIGNMENT_DATE("SHIP_SCHEDULE_ERR_006", 400),
  ASSIGNMENT_EMPTY("SHIP_SCHEDULE_ERR_007", 400),
  ASSIGNMENT_INVALID_RANGE("SHIP_SCHEDULE_ERR_008", 400),

  // ===================== Conflict / overlap =====================
  CREW_ALREADY_ASSIGNED("SHIP_SCHEDULE_ERR_009", 409),
  CREW_ASSIGNMENT_OVERLAP("SHIP_SCHEDULE_ERR_010", 409),
  CREW_CONFLICT_WITH_SHIP_SCHEDULE("SHIP_SCHEDULE_ERR_011", 409),

  // ===================== Schedule validation =====================
  SCHEDULE_NOT_FOUND("SHIP_SCHEDULE_ERR_012", 404),
  SCHEDULE_INVALID_STATE("SHIP_SCHEDULE_ERR_013", 400),

  // ===================== Permission / business rule =====================
  SCHEDULE_CREATION_FORBIDDEN("SHIP_SCHEDULE_ERR_014", 403),
  CONTRACT_NOT_ACTIVE_FOR_SHIP("SHIP_SCHEDULE_ERR_015", 400);

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

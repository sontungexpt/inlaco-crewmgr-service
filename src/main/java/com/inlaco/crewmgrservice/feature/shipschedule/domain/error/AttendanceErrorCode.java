package com.inlaco.crewmgrservice.feature.shipschedule.domain.error;

import com.inlaco.crewmgrservice.shared.kernel.error.HttpMappableErrorCode;

public enum AttendanceErrorCode implements HttpMappableErrorCode {
  ATTENDANCE_UNKNOWN_ERROR("ATTENDANCE_000", 500),

  // QR errors
  ATTENDANCE_QR_NOT_FOUND("ATTENDANCE_QR_001", 404),

  ATTENDANCE_QR_EXPIRED("ATTENDANCE_QR_002", 400),

  ATTENDANCE_QR_INVALID("ATTENDANCE_QR_003", 400),

  // Attendance state errors
  ATTENDANCE_ALREADY_CHECKED_IN("ATTENDANCE_STATE_001", 400),

  ATTENDANCE_ALREADY_CHECKED_OUT("ATTENDANCE_STATE_002", 400),

  ATTENDANCE_INVALID_SEQUENCE("ATTENDANCE_STATE_003", 400),

  // Device errors
  ATTENDANCE_DEVICE_ALREADY_USED("ATTENDANCE_DEVICE_001", 400);

  private final String code;

  private final int status;

  AttendanceErrorCode(String code, int status) {
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

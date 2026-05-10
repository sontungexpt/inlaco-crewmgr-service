package com.inlaco.crewmgrservice.feature.shipschedule.domain.exception;

import com.inlaco.crewmgrservice.shared.kernel.error.ErrorCode;
import com.inlaco.crewmgrservice.shared.kernel.exception.ApplicationException;
import lombok.Getter;

@Getter
public class AttendanceQRCodeException extends ApplicationException {

  public AttendanceQRCodeException(ErrorCode errorCode, String message) {
    super(errorCode, message);
  }
}

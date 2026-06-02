package com.inlaco.crewmgrservice.feature.shipschedule.domain.exception;

import com.inlaco.crewmgrservice.shared.kernel.error.ErrorCode;
import com.inlaco.crewmgrservice.shared.kernel.exception.ApplicationException;
import lombok.Getter;

@Getter
public class ShipScheduleException extends ApplicationException {

  public ShipScheduleException(ErrorCode errorCode, String message) {
    super(errorCode, message);
  }

  public ShipScheduleException(ErrorCode errorCode, String message, Object data) {
    super(errorCode, message, data);
  }
}

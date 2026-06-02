package com.inlaco.crewmgrservice.feature.ship.domain.exception;

import com.inlaco.crewmgrservice.feature.ship.domain.error.ShipErrorCode;
import com.inlaco.crewmgrservice.shared.kernel.exception.ApplicationException;
import java.util.Map;

public class ShipException extends ApplicationException {
  public ShipException(ShipErrorCode errorCode, String message) {
    super(errorCode, message, Map.of());
  }

  public ShipException(ShipErrorCode errorCode, String message, Map<String, Object> details) {
    super(errorCode, message, details);
  }
}

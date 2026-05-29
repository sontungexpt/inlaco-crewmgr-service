package com.inlaco.crewmgrservice.feature.crewmobilization.domain.exception;

import com.inlaco.crewmgrservice.feature.crewmobilization.domain.error.CrewMobilizationErrorCode;
import com.inlaco.crewmgrservice.shared.kernel.exception.ApplicationException;

public class CrewMobilizationValidationException extends ApplicationException {

  public CrewMobilizationValidationException(CrewMobilizationErrorCode errorCode, String message) {
    super(errorCode, message);
  }

  public CrewMobilizationValidationException(
      CrewMobilizationErrorCode errorCode, String message, Object details) {

    super(errorCode, message, details);
  }
}

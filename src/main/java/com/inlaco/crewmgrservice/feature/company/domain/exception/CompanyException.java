package com.inlaco.crewmgrservice.feature.company.domain.exception;

import com.inlaco.crewmgrservice.feature.company.domain.error.CompanyErrorCode;
import com.inlaco.crewmgrservice.shared.kernel.exception.ApplicationException;
import java.util.Map;

public class CompanyException extends ApplicationException {

  public CompanyException(CompanyErrorCode errorCode, String message) {
    super(errorCode, message);
  }

  public CompanyException(CompanyErrorCode errorCode, String message, Map<String, Object> details) {
    super(errorCode, message, details);
  }
}

package com.inlaco.crewmgrservice.shared.kernel.error;

public interface HttpMappableErrorCode extends ErrorCode {
  int httpStatus();
}

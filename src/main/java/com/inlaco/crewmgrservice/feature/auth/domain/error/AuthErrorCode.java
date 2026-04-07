package com.inlaco.crewmgrservice.feature.auth.domain.error;

import com.inlaco.crewmgrservice.shared.kernel.error.HttpMappableErrorCode;

public enum AuthErrorCode implements HttpMappableErrorCode {
  AUTH_UNKNOWN_ERROR("AUTH_000", 500),
  AUTH_REFRESH_TOKEN_NOT_FOUND("AUTH_REFRESH_001", 401),
  AUTH_REFRESH_TOKEN_EXPIRED("AUTH_REFRESH_002", 401),
  AUTH_REFRESH_TOKEN_REVOKED("AUTH_REFRESH_003", 401),
  AUTH_DISABLED_USER("AUTH_001", 403),
  AUTH_LOCKED_USER("AUTH_002", 403),
  ;
  private final String code;
  private final int status;

  AuthErrorCode(String code, int status) {
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

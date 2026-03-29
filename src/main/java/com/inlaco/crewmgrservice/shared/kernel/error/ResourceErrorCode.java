package com.inlaco.crewmgrservice.shared.kernel.error;

public enum ResourceErrorCode implements HttpMappableErrorCode {
  RESOURCE_NOT_FOUND("RESOURCE_ERR_1", 404),
  RESOURCE_ALREADY_IN_USE("RESOURCE_ERR_2", 409),
  RESOURCE_DELETE_FAILED("RESOURCE_ERR_3", 500),
  ;

  private String code;
  private int httpStatus;

  private ResourceErrorCode(String code, int httpStatus) {
    this.code = code;
    this.httpStatus = httpStatus;
  }

  @Override
  public String code() {
    return code;
  }

  @Override
  public int httpStatus() {
    return httpStatus;
  }
}

package com.inlaco.crewmgrservice.feature.company.domain.error;

import com.inlaco.crewmgrservice.shared.kernel.error.HttpMappableErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum CompanyErrorCode implements HttpMappableErrorCode {
  COMPANY_NOT_FOUND("COMPANY_NOT_FOUND", "Company not found", HttpStatus.NOT_FOUND),
  COMPANY_ALREADY_EXISTS(
      "COMPANY_ALREADY_EXISTS",
      "Company with this registration number already exists",
      HttpStatus.CONFLICT),
  INVALID_COMPANY_STATUS(
      "INVALID_COMPANY_STATUS", "Invalid company status transition", HttpStatus.BAD_REQUEST),
  COMPANY_NAME_REQUIRED(
      "COMPANY_NAME_REQUIRED", "Company name is required", HttpStatus.BAD_REQUEST),
  REGISTRATION_NUMBER_REQUIRED(
      "REGISTRATION_NUMBER_REQUIRED", "Registration number is required", HttpStatus.BAD_REQUEST),
  INVALID_EMAIL_FORMAT("INVALID_EMAIL_FORMAT", "Invalid email format", HttpStatus.BAD_REQUEST),
  INVALID_PHONE_NUMBER(
      "INVALID_PHONE_NUMBER", "Invalid phone number format", HttpStatus.BAD_REQUEST);

  private final String code;
  private final String message;
  private final HttpStatus httpStatus;

  CompanyErrorCode(String code, String message, HttpStatus httpStatus) {
    this.code = code;
    this.message = message;
    this.httpStatus = httpStatus;
  }

  @Override
  public String code() {
    return code;
  }

  @Override
  public int httpStatus() {
    return httpStatus.value();
  }
}

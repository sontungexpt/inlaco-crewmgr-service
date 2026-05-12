package com.inlaco.crewmgrservice.feature.ship.domain.error;

import com.inlaco.crewmgrservice.shared.kernel.error.HttpMappableErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ShipErrorCode implements HttpMappableErrorCode {
  SHIP_NOT_FOUND("SHIP_NOT_FOUND", "Ship not found", HttpStatus.NOT_FOUND),
  SHIP_ALREADY_EXISTS(
      "SHIP_ALREADY_EXISTS", "Ship with this IMO number already exists", HttpStatus.CONFLICT),
  INVALID_SHIP_STATUS(
      "INVALID_SHIP_STATUS", "Invalid ship status transition", HttpStatus.BAD_REQUEST),
  SHIP_NAME_REQUIRED("SHIP_NAME_REQUIRED", "Ship name is required", HttpStatus.BAD_REQUEST),
  IMO_NUMBER_REQUIRED("IMO_NUMBER_REQUIRED", "IMO number is required", HttpStatus.BAD_REQUEST),
  INVALID_IMO_NUMBER("INVALID_IMO_NUMBER", "Invalid IMO number format", HttpStatus.BAD_REQUEST),
  CREW_CAPACITY_EXCEEDED(
      "CREW_CAPACITY_EXCEEDED", "Crew capacity exceeded", HttpStatus.BAD_REQUEST),
  SHIP_NOT_AVAILABLE(
      "SHIP_NOT_AVAILABLE", "Ship is not available for scheduling", HttpStatus.BAD_REQUEST),
  INVALID_EMAIL_FORMAT("INVALID_EMAIL_FORMAT", "Invalid email format", HttpStatus.BAD_REQUEST),
  INVALID_PHONE_NUMBER(
      "INVALID_PHONE_NUMBER", "Invalid phone number format", HttpStatus.BAD_REQUEST);

  private final String code;
  private final String message;
  private final HttpStatus httpStatus;

  ShipErrorCode(String code, String message, HttpStatus httpStatus) {
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

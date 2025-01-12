package com.inlaco.crewmgrservice.feature.course.exception;

import org.springframework.http.HttpStatus;

public class RegistrationClosedException extends CourseException {

  public RegistrationClosedException(String message) {
    super(HttpStatus.FORBIDDEN, message);
  }
}

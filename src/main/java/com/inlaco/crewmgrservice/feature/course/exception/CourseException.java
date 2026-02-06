package com.inlaco.crewmgrservice.feature.course.exception;

import com.inlaco.crewmgrservice.application.exception.ApplicationExceptionException;
import org.springframework.http.HttpStatus;

public abstract class CourseException extends ApplicationExceptionException {

  public CourseException(HttpStatus status, String message) {
    super("COURSE_ERROR", message, status);
  }
}

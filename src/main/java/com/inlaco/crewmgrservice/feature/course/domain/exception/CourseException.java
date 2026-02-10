package com.inlaco.crewmgrservice.feature.course.domain.exception;

import com.inlaco.crewmgrservice.application.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public abstract class CourseException extends ApplicationException {

  public CourseException(HttpStatus status, String message) {
    super("COURSE_ERROR", message, status);
  }
}

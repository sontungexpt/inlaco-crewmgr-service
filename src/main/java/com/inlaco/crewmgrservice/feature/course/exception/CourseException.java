package com.inlaco.crewmgrservice.feature.course.exception;

import com.inlaco.crewmgrservice.domain.exception.BaseException;
import org.springframework.http.HttpStatus;

public abstract class CourseException extends BaseException {

  public CourseException(HttpStatus status, String message) {
    super("COURSE_ERROR", message, status);
  }
}

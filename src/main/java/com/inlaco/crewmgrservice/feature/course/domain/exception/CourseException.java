package com.inlaco.crewmgrservice.feature.course.domain.exception;

import com.inlaco.crewmgrservice.shared.kernel.exception.ApplicationException;

public abstract class CourseException extends ApplicationException {

  public CourseException(String message) {
    super("COURSE_ERROR", message);
  }
}

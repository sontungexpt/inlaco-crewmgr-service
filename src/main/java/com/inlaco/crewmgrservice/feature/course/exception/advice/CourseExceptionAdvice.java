package com.inlaco.crewmgrservice.feature.course.exception.advice;

import com.inlaco.crewmgrservice.common.payload.ExceptionResponse;
import com.inlaco.crewmgrservice.feature.course.exception.CourseException;
import com.inlaco.crewmgrservice.feature.course.exception.RegistrationClosedException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CourseExceptionAdvice {

  @ExceptionHandler({RegistrationClosedException.class})
  public ResponseEntity<?> handleRegistrationClosedException(
      RegistrationClosedException ex, HttpServletRequest request) {
    return ExceptionResponse.builder(ex).request(request).build().toResponseEntity();
  }

  @ExceptionHandler({CourseException.class})
  public ResponseEntity<?> handleCourseException(CourseException ex, HttpServletRequest request) {
    return ExceptionResponse.builder(ex).request(request).build().toResponseEntity();
  }
}

package com.inlaco.crewmgrservice.infrastructure.web.advice;

import com.inlaco.crewmgrservice.domain.exception.ResourceAlreadyInUseException;
import com.inlaco.crewmgrservice.domain.exception.ResourceNotFoundException;
import com.inlaco.crewmgrservice.infrastructure.web.payload.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ResourceExceptionAdvice {

  @ExceptionHandler(ResourceAlreadyInUseException.class)
  public ErrorResponse handleResourceAlreadyInUse(
      ResourceAlreadyInUseException ex, HttpServletRequest request) {
    log.debug("Resource already in use: {}", ex.getMessage());
    return buildResponse(HttpStatus.CONFLICT, "RESOURCE_ALREADY_IN_USE", ex.getMessage(), request);
  }

  @ExceptionHandler(ResourceNotFoundException.class)
  public ErrorResponse handleResourceNotFound(
      ResourceNotFoundException ex, HttpServletRequest request) {
    log.debug("Resource not found: {}", ex.getMessage());
    return buildResponse(HttpStatus.NOT_FOUND, "RESOURCE_NOT_FOUND", ex.getMessage(), request);
  }

  private ErrorResponse buildResponse(
      HttpStatus status, String errorCode, String message, HttpServletRequest request) {
    return ErrorResponse.of(status)
        .errorCode(errorCode)
        .message(message)
        .path(request.getRequestURI())
        .build();
  }
}

package com.inlaco.crewmgrservice.infrastructure.web.advice;

import com.inlaco.crewmgrservice.infrastructure.web.payload.response.ValidationErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ValidationException;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

@RestControllerAdvice
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ValidationExceptionAdvice {

  @ExceptionHandler(BindException.class)
  public ResponseEntity<?> handleBindException(BindException e, HttpServletRequest request) {
    final Map<String, String> body = new HashMap<>();
    e.getFieldErrors()
        .forEach(fieldError -> body.put(fieldError.getField(), fieldError.getDefaultMessage()));
    log.debug("Validation error: {}", body);
    return ValidationErrorResponse.of(HttpStatus.BAD_REQUEST)
        .errorCode("VALIDATION_ERROR")
        .path(request.getRequestURI())
        .data(body)
        .build()
        .toResponseEntity();
  }

  @ExceptionHandler(HandlerMethodValidationException.class)
  public ResponseEntity<?> handleHandlerMethodValidationException(
      HandlerMethodValidationException e, HttpServletRequest request) {
    final Map<String, String> body = new HashMap<>();
    e.getAllErrors()
        .forEach(
            objectError -> {
              final String[] fieldError = objectError.getCodes()[0].split("\\.");
              body.put(fieldError[fieldError.length - 1], objectError.getDefaultMessage());
            });
    log.debug("Validation error: {}", body);
    return ValidationErrorResponse.of(HttpStatus.BAD_REQUEST)
        .errorCode("VALIDATION_ERROR")
        .data(body)
        .path(request.getRequestURI())
        .build()
        .toResponseEntity();
  }

  @ExceptionHandler({IllegalAccessException.class, IllegalArgumentException.class})
  public ResponseEntity<?> handleIllegalArgumentException(Exception e, HttpServletRequest request) {
    return AdviceUtils.buildErrorResponse(
        HttpStatus.BAD_REQUEST, "ILLEGAL_AGRS_ERROR", "Illegal Args Error", request);
  }

  @ExceptionHandler({BadRequestException.class})
  public ResponseEntity<?> handleBadRequestException(
      BadRequestException ex, HttpServletRequest request) {
    return AdviceUtils.buildErrorResponse(
        HttpStatus.BAD_REQUEST, "BAD_REQUEST_ERROR", "Bad request", request);
  }

  @ExceptionHandler(ValidationException.class)
  public ResponseEntity<?> handleValidationException(
      ValidationException e, HttpServletRequest request) {
    return AdviceUtils.buildErrorResponse(
        HttpStatus.BAD_REQUEST, "VALIDATION_ERROR", "Validation failed", request);
  }
}

package com.inlaco.crewmgrservice.infrastructure.web.advice;

import com.inlaco.crewmgrservice.application.exception.ApplicationExceptionException;
import com.inlaco.crewmgrservice.infrastructure.web.payload.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

@RestControllerAdvice
@Order(Ordered.LOWEST_PRECEDENCE)
@Slf4j
public class FallbackExceptionAdvice {

  // ===================== BUSINESS =====================
  @ExceptionHandler(ApplicationExceptionException.class)
  public ResponseEntity<ApiResponse<Void>> handleBaseException(
      ApplicationExceptionException ex, HttpServletRequest request) {
    log.warn("Business exception [{}]: {}", ex.getErrorCode(), ex.getMessage());
    return AdviceUtils.buildErrorResponse(
        ex.getStatus(), ex.getErrorCode(), ex.getMessage(), request);
  }

  // ===================== CLIENT INPUT =====================
  @ExceptionHandler(MissingServletRequestPartException.class)
  public ResponseEntity<ApiResponse<Void>> handleMissingPart(
      MissingServletRequestPartException ex, HttpServletRequest request) {
    return AdviceUtils.buildErrorResponse(
        HttpStatus.BAD_REQUEST, "MISSING_REQUEST_PART", "Missing request part", request);
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ApiResponse<Void>> handleInvalidBody(
      HttpMessageNotReadableException ex, HttpServletRequest request) {
    return AdviceUtils.buildErrorResponse(
        HttpStatus.UNPROCESSABLE_ENTITY, "INVALID_REQUEST_BODY", "Invalid request body", request);
  }

  // ===================== INFRA / TECH =====================
  @ExceptionHandler(UnsupportedOperationException.class)
  public ResponseEntity<ApiResponse<Void>> handleUnsupported(
      UnsupportedOperationException ex, HttpServletRequest request) {
    log.error("Unsupported operation", ex);
    return AdviceUtils.buildErrorResponse(
        HttpStatus.NOT_IMPLEMENTED, "UNSUPPORTED_OPERATION", "Operation is not supported", request);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiResponse<Void>> handleUnknown(Exception ex, HttpServletRequest request) {
    log.error("Unhandled exception", ex);
    return AdviceUtils.buildErrorResponse(
        HttpStatus.INTERNAL_SERVER_ERROR,
        "INTERNAL_SERVER_ERROR",
        "System is meetting unexpected error",
        request);
  }
}

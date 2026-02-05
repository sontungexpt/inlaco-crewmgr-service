package com.inlaco.crewmgrservice.infrastructure.web.advice;

import com.inlaco.crewmgrservice.domain.exception.BaseException;
import com.inlaco.crewmgrservice.infrastructure.web.payload.response.ApiResponse;
import com.inlaco.crewmgrservice.infrastructure.web.payload.response.ErrorResponse;
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
  @ExceptionHandler(BaseException.class)
  public ResponseEntity<ApiResponse<Void>> handleBaseException(
      BaseException ex, HttpServletRequest request) {
    log.warn("Business exception [{}]: {}", ex.getErrorCode(), ex.getMessage());
    return ErrorResponse.of(ex.getStatus())
        .code(ex.getStatus().value())
        .errorCode(ex.getErrorCode())
        .message(ex.getMessage())
        .path(request.getRequestURI())
        .build()
        .toResponseEntity();
  }

  // ===================== CLIENT INPUT =====================
  @ExceptionHandler(MissingServletRequestPartException.class)
  public ResponseEntity<ApiResponse<Void>> handleMissingPart(
      MissingServletRequestPartException ex, HttpServletRequest request) {
    return buildError(
        HttpStatus.BAD_REQUEST, "MISSING_REQUEST_PART", "Missing request part", request);
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ApiResponse<Void>> handleInvalidBody(
      HttpMessageNotReadableException ex, HttpServletRequest request) {
    return buildError(
        HttpStatus.UNPROCESSABLE_ENTITY, "INVALID_REQUEST_BODY", "Invalid request body", request);
  }

  // ===================== INFRA / TECH =====================
  @ExceptionHandler(UnsupportedOperationException.class)
  public ResponseEntity<ApiResponse<Void>> handleUnsupported(
      UnsupportedOperationException ex, HttpServletRequest request) {
    log.error("Unsupported operation", ex);
    return buildError(
        HttpStatus.NOT_IMPLEMENTED, "UNSUPPORTED_OPERATION", "Operation is not supported", request);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiResponse<Void>> handleUnknown(Exception ex, HttpServletRequest request) {
    log.error("Unhandled exception", ex);
    return buildError(
        HttpStatus.INTERNAL_SERVER_ERROR,
        "INTERNAL_SERVER_ERROR",
        "System is meetting unexpected error",
        request);
  }

  // ===================== HELPERS =====================

  private ResponseEntity<ApiResponse<Void>> buildError(
      HttpStatus status, String errorCode, String message, HttpServletRequest request) {
    return ErrorResponse.of(status)
        .code(status.value())
        .errorCode(errorCode)
        .message(message)
        .path(request.getRequestURI())
        .build()
        .toResponseEntity();
  }
}

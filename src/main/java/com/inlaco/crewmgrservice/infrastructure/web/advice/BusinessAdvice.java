package com.inlaco.crewmgrservice.infrastructure.web.advice;

import com.inlaco.crewmgrservice.infrastructure.web.payload.response.ApiResponse;
import com.inlaco.crewmgrservice.shared.kernel.error.ErrorCode;
import com.inlaco.crewmgrservice.shared.kernel.error.HttpMappableErrorCode;
import com.inlaco.crewmgrservice.shared.kernel.exception.ApplicationException;
import com.inlaco.crewmgrservice.shared.kernel.exception.ResourceAlreadyInUseException;
import com.inlaco.crewmgrservice.shared.kernel.exception.ResourceDeleteFailedException;
import com.inlaco.crewmgrservice.shared.kernel.exception.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class BusinessAdvice {

  @ExceptionHandler(ResourceAlreadyInUseException.class)
  public ResponseEntity<ApiResponse<Void>> handleResourceAlreadyInUse(
      ResourceAlreadyInUseException ex, HttpServletRequest request) {
    log.debug("Resource already in use: {}", ex.getMessage());
    return AdviceUtils.buildErrorResponse(
        HttpStatus.CONFLICT, "RESOURCE_ALREADY_IN_USE", ex.getMessage(), request);
  }

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<ApiResponse<Void>> handleResourceNotFound(
      ResourceNotFoundException ex, HttpServletRequest request) {
    log.debug("Resource not found: {}", ex.getMessage());
    return AdviceUtils.buildErrorResponse(
        HttpStatus.NOT_FOUND, "RESOURCE_NOT_FOUND", ex.getMessage(), request);
  }

  public ResponseEntity<ApiResponse<Void>> handleResourceDeleteFailed(
      ResourceDeleteFailedException ex, HttpServletRequest request) {
    log.debug("Resource delete failed: {}", ex.getMessage());
    return AdviceUtils.buildErrorResponse(
        HttpStatus.CONFLICT, "RESOURCE_DELETE_FAILED", ex.getMessage(), request);
  }

  // ===================== BUSINESS =====================
  @ExceptionHandler(ApplicationException.class)
  public ResponseEntity<ApiResponse<Object>> handleBaseException(
      ApplicationException ex, HttpServletRequest request) {
    String errorCode = ex.getErrorCode() != null ? ex.getErrorCode().code() : ex.getErrorCodeStr();
    log.warn("Business exception [{}]: {}", errorCode, ex.getMessage());
    return AdviceUtils.buildErrorResponse(
        resolveStatus(ex), errorCode, ex.getMessage(), ex.getData(), request);
  }

  private HttpStatus resolveStatus(ApplicationException ex) {
    ErrorCode errorCode = ex.getErrorCode();

    if (errorCode instanceof HttpMappableErrorCode e) {
      return HttpStatus.valueOf(e.httpStatus());
    }

    return switch (ex.getErrorCode()) {
      default -> HttpStatus.BAD_REQUEST;
    };
  }
}

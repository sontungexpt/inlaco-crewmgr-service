package com.inlaco.crewmgrservice.infrastructure.web.advice;

import com.inlaco.crewmgrservice.infrastructure.web.payload.response.ApiResponse;
import com.inlaco.crewmgrservice.shared.kernel.exception.ApplicationException;
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

  // ===================== BUSINESS =====================
  @ExceptionHandler(ApplicationException.class)
  public ResponseEntity<ApiResponse<Void>> handleBaseException(
      ApplicationException ex, HttpServletRequest request) {
    log.warn("Business exception [{}]: {}", ex.getErrorCode(), ex.getMessage());
    return AdviceUtils.buildErrorResponse(
        HttpStatus.INTERNAL_SERVER_ERROR, ex.getErrorCodeStr(), ex.getMessage(), request);
  }
}

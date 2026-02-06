package com.inlaco.crewmgrservice.infrastructure.web.advice;

import com.inlaco.crewmgrservice.infrastructure.web.payload.response.ApiResponse;
import com.inlaco.crewmgrservice.infrastructure.web.payload.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

final class AdviceUtils {
  public static ResponseEntity<ApiResponse<Void>> buildErrorResponse(
      HttpStatus status, String errorCode, String message, HttpServletRequest request) {
    return ErrorResponse.<Void>of(status)
        .errorCode(errorCode)
        .message(message)
        .path(request.getRequestURI())
        .build()
        .toResponseEntity();
  }
}

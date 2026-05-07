//package com.inlaco.crewmgrservice.feature.totp.presentation.rest.advice;
//
//import com.inlaco.crewmgrservice.feature.totp.domain.exception.TotpDisabledException;
//import com.inlaco.crewmgrservice.feature.totp.domain.exception.TotpExpiredException;
//import com.inlaco.crewmgrservice.feature.totp.domain.exception.TotpInvalidCodeException;
//import com.inlaco.crewmgrservice.feature.totp.domain.exception.TotpNotFoundException;
//import com.inlaco.crewmgrservice.feature.totp.domain.exception.TotpSetupFailedException;
//import com.inlaco.crewmgrservice.infrastructure.web.advice.AdviceUtils;
//import com.inlaco.crewmgrservice.infrastructure.web.payload.response.ApiResponse;
//import jakarta.servlet.http.HttpServletRequest;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.core.Ordered;
//import org.springframework.core.annotation.Order;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//
//@RestControllerAdvice
//@Order(Ordered.HIGHEST_PRECEDENCE)
//@Slf4j
//public class TotpExceptionAdvice {
//
//  @ExceptionHandler(TotpNotFoundException.class)
//  public ResponseEntity<ApiResponse<Void>> handleTotpNotFound(
//      TotpNotFoundException ex, HttpServletRequest request) {
//    log.warn("TOTP not found: {}", ex.getMessage());
//    return AdviceUtils.buildErrorResponse(
//        HttpStatus.NOT_FOUND, ex.getErrorCode().code(), ex.getMessage(), request);
//  }
//
//  @ExceptionHandler(TotpDisabledException.class)
//  public ResponseEntity<ApiResponse<Void>> handleTotpDisabled(
//      TotpDisabledException ex, HttpServletRequest request) {
//    log.warn("TOTP disabled: {}", ex.getMessage());
//    return AdviceUtils.buildErrorResponse(
//        HttpStatus.FORBIDDEN, ex.getErrorCode().code(), ex.getMessage(), request);
//  }
//
//  @ExceptionHandler(TotpInvalidCodeException.class)
//  public ResponseEntity<ApiResponse<Void>> handleTotpInvalidCode(
//      TotpInvalidCodeException ex, HttpServletRequest request) {
//    log.warn("Invalid TOTP code: {}", ex.getMessage());
//    return AdviceUtils.buildErrorResponse(
//        HttpStatus.BAD_REQUEST, ex.getErrorCode().code(), ex.getMessage(), request);
//  }
//
//  @ExceptionHandler(TotpExpiredException.class)
//  public ResponseEntity<ApiResponse<Void>> handleTotpExpired(
//      TotpExpiredException ex, HttpServletRequest request) {
//    log.warn("TOTP expired: {}", ex.getMessage());
//    return AdviceUtils.buildErrorResponse(
//        HttpStatus.BAD_REQUEST, ex.getErrorCode().code(), ex.getMessage(), request);
//  }
//
//  @ExceptionHandler(TotpSetupFailedException.class)
//  public ResponseEntity<ApiResponse<Void>> handleTotpSetupFailed(
//      TotpSetupFailedException ex, HttpServletRequest request) {
//    log.error("TOTP setup failed: {}", ex.getMessage());
//    return AdviceUtils.buildErrorResponse(
//        HttpStatus.INTERNAL_SERVER_ERROR, ex.getErrorCode().code(), ex.getMessage(), request);
//  }
//}

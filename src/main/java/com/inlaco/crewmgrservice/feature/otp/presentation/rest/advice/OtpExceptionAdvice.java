//package com.inlaco.crewmgrservice.feature.otp.presentation.rest.advice;
//
//import com.inlaco.crewmgrservice.feature.otp.domain.exception.OtpExpiredException;
//import com.inlaco.crewmgrservice.feature.otp.domain.exception.OtpInvalidException;
//import com.inlaco.crewmgrservice.feature.otp.domain.exception.OtpNotFoundException;
//import com.inlaco.crewmgrservice.feature.otp.domain.exception.OtpSendFailedException;
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
//public class OtpExceptionAdvice {
//
//  @ExceptionHandler(OtpNotFoundException.class)
//  public ResponseEntity<ApiResponse<Void>> handleOtpNotFound(
//      OtpNotFoundException ex, HttpServletRequest request) {
//    log.warn("OTP not found: {}", ex.getMessage());
//    return AdviceUtils.buildErrorResponse(
//        HttpStatus.NOT_FOUND, ex.getErrorCode().code(), ex.getMessage(), request);
//  }
//
//  @ExceptionHandler(OtpExpiredException.class)
//  public ResponseEntity<ApiResponse<Void>> handleOtpExpired(
//      OtpExpiredException ex, HttpServletRequest request) {
//    log.warn("OTP expired: {}", ex.getMessage());
//    return AdviceUtils.buildErrorResponse(
//        HttpStatus.BAD_REQUEST, ex.getErrorCode().code(), ex.getMessage(), request);
//  }
//
//  @ExceptionHandler(OtpInvalidException.class)
//  public ResponseEntity<ApiResponse<Void>> handleOtpInvalid(
//      OtpInvalidException ex, HttpServletRequest request) {
//    log.warn("Invalid OTP: {}", ex.getMessage());
//    return AdviceUtils.buildErrorResponse(
//        HttpStatus.BAD_REQUEST, ex.getErrorCode().code(), ex.getMessage(), request);
//  }
//
//  @ExceptionHandler(OtpSendFailedException.class)
//  public ResponseEntity<ApiResponse<Void>> handleOtpSendFailed(
//      OtpSendFailedException ex, HttpServletRequest request) {
//    log.error("OTP send failed: {}", ex.getMessage());
//    return AdviceUtils.buildErrorResponse(
//        HttpStatus.INTERNAL_SERVER_ERROR, ex.getErrorCode().code(), ex.getMessage(), request);
//  }
//}

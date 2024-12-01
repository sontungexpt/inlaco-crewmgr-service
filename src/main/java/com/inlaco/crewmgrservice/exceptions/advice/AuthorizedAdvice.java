package com.inlaco.crewmgrservice.exceptions.advice;

import com.inlaco.crewmgrservice.common.payload.ExceptionResponse;
import com.inlaco.crewmgrservice.exceptions.JwtTokenException;
import com.inlaco.crewmgrservice.feature.user.enums.UserStatus;
import jakarta.servlet.http.HttpServletRequest;
import java.nio.file.AccessDeniedException;
import java.util.Map;
import javax.security.auth.login.AccountExpiredException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class AuthorizedAdvice {

  @ExceptionHandler({
    UsernameNotFoundException.class,
    BadCredentialsException.class,
  })
  public ResponseEntity<?> handleUserLoginException(Exception ex, HttpServletRequest request) {
    return ExceptionResponse.builder(ex, HttpStatus.NOT_FOUND)
        .message("Password or username is incorrect")
        .request(request)
        .build()
        .toResponseEntity();
  }

  @ExceptionHandler({DisabledException.class})
  public ResponseEntity<?> handleDisabledException(
      DisabledException ex, HttpServletRequest request) {
    return ExceptionResponse.builder(ex, HttpStatus.FORBIDDEN)
        .request(request)
        .data(Map.of("reason", UserStatus.UNVERIFIED, "message", "Email not verified"))
        .build()
        .toResponseEntity();
  }

  @ExceptionHandler({AccountExpiredException.class})
  public ResponseEntity<?> handleAccountExpiredException(
      AccountExpiredException ex, HttpServletRequest request) {
    return ExceptionResponse.builder(ex, HttpStatus.FORBIDDEN)
        .request(request)
        .data(Map.of("reason", UserStatus.EXPIRED, "message", "Account expired"))
        .build()
        .toResponseEntity();
  }

  // Handle AccessDeniedException with 403 Forbidden
  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<?> handleAccessDeniedException(
      AccessDeniedException ex, HttpServletRequest request) {
    return ExceptionResponse.builder(ex, HttpStatus.FORBIDDEN)
        .request(request)
        .build()
        .toResponseEntity();
  }

  // Handle LockedException with a specific status code, such as 423 Locked
  @ExceptionHandler(LockedException.class)
  public ResponseEntity<?> handleLockedException(LockedException ex, HttpServletRequest request) {
    return ExceptionResponse.builder(ex, HttpStatus.LOCKED)
        .request(request)
        .data(Map.of("reason", UserStatus.LOCKED, "message", "Account locked"))
        .build()
        .toResponseEntity();
  }

  @ExceptionHandler(AuthenticationException.class)
  public ResponseEntity<?> handleAuthenticationException(
      AuthenticationException ex, HttpServletRequest request) {
    return ExceptionResponse.builder(ex, HttpStatus.UNAUTHORIZED)
        .request(request)
        .build()
        .toResponseEntity();
  }

  @ExceptionHandler(JwtTokenException.class)
  public ResponseEntity<?> handleInvalidTokenException(
      JwtTokenException ex, HttpServletRequest request) {
    return ExceptionResponse.builder(ex, HttpStatus.UNAUTHORIZED)
        .request(request)
        .build()
        .toResponseEntity();
  }
}

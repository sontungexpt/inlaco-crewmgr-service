package com.inlaco.crewmgrservice.infrastructure.web.advice;

import com.inlaco.crewmgrservice.infrastructure.security.jwt.exception.JwtTokenException;
import jakarta.servlet.http.HttpServletRequest;
import javax.security.auth.login.AccountExpiredException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
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

  @ExceptionHandler({UsernameNotFoundException.class, BadCredentialsException.class})
  public ResponseEntity<?> handleBadCredentials(Exception ex, HttpServletRequest request) {
    // TODO: when frontend fixed by using errorCode change status to UNAUTHORIZED
    return AdviceUtils.buildErrorResponse(
        HttpStatus.NOT_FOUND, "INVALID_CREDENTIALS", "Invalid credentials", request);
  }

  @ExceptionHandler(DisabledException.class)
  public ResponseEntity<?> handleDisabled(DisabledException ex, HttpServletRequest request) {
    return AdviceUtils.buildErrorResponse(
        HttpStatus.FORBIDDEN, "ACCOUNT_DISABLED", "Account disabled", request);
  }

  @ExceptionHandler(AccountExpiredException.class)
  public ResponseEntity<?> handleExpired(AccountExpiredException ex, HttpServletRequest request) {
    return AdviceUtils.buildErrorResponse(
        HttpStatus.FORBIDDEN, "ACCOUNT_EXPIRED", "Account expired", request);
  }

  @ExceptionHandler(LockedException.class)
  public ResponseEntity<?> handleLocked(LockedException ex, HttpServletRequest request) {
    return AdviceUtils.buildErrorResponse(
        HttpStatus.LOCKED, "ACCOUNT_LOCKED", "Account locked", request);
  }

  @ExceptionHandler(JwtTokenException.class)
  public ResponseEntity<?> handleJwt(JwtTokenException ex, HttpServletRequest request) {
    return AdviceUtils.buildErrorResponse(
        HttpStatus.UNAUTHORIZED, "INVALID_TOKEN", "Token malformed or expired", request);
  }

  @ExceptionHandler({AccessDeniedException.class})
  public ResponseEntity<?> handleAccessDenied(
      AccessDeniedException ex, HttpServletRequest request) {
    return AdviceUtils.buildErrorResponse(
        HttpStatus.FORBIDDEN, "ACCESS_DENIED", "You are not have permission", request);
  }

  @ExceptionHandler(AuthenticationException.class)
  public ResponseEntity<?> handleAuthenticationException(
      AuthenticationException ex, HttpServletRequest request) {
    return AdviceUtils.buildErrorResponse(
        HttpStatus.UNAUTHORIZED, "UNAUTHORIZED", "Unauthorized", request);
  }
}

package com.inlaco.crewmgrservice.infrastructure.security.jwt.exception;

import lombok.Getter;

/**
 * Thrown when an invalid token request is made. For example, when the token is expired or the token
 * is malformed.
 */
@Getter
public class JwtTokenException extends RuntimeException {

  private static final String TOKEN_TYPE = "BEARER";
  private final String token;

  public JwtTokenException(String token, String message) {
    super(String.format("%s: [%s] token: [%s] ", message, TOKEN_TYPE, token));
    this.token = token;
  }
}

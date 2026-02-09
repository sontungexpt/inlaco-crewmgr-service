package com.inlaco.crewmgrservice.feature.auth.domain.exception;

import com.inlaco.crewmgrservice.feature.auth.domain.model.RefreshToken;
import lombok.Getter;

@Getter
public class RefreshTokenException extends RuntimeException {

  private final RefreshToken token;

  public RefreshTokenException(RefreshToken token, String msg) {
    super(msg);
    this.token = token;
  }
}

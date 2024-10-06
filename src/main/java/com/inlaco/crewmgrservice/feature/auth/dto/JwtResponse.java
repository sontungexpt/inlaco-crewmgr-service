package com.inlaco.crewmgrservice.feature.auth.dto;

import com.inlaco.crewmgrservice.feature.auth.enums.TokenType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtResponse {

  private String accessToken;

  private String refreshToken;

  private TokenType tokenType = TokenType.BEARER;

  public JwtResponse(String accessToken, String refreshToken) {
    this.accessToken = accessToken;
    this.refreshToken = refreshToken;
  }
}

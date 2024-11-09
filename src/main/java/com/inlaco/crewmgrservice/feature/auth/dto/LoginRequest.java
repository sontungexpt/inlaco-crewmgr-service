package com.inlaco.crewmgrservice.feature.auth.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.inlaco.crewmgrservice.validation.annotation.Password;
import com.inlaco.crewmgrservice.validation.annotation.PhoneNumber;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
  @JsonAlias("username")
  @PhoneNumber
  private String phoneNumber;

  public String getUsername() {
    return phoneNumber;
  }

  @Password private String password;
}

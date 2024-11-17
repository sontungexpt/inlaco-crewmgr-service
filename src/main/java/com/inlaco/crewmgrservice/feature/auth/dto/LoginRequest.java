package com.inlaco.crewmgrservice.feature.auth.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.inlaco.crewmgrservice.validation.annotation.Password;
import com.inlaco.crewmgrservice.validation.annotation.PhoneNumber;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest implements Serializable {

  @JsonAlias("username")
  @PhoneNumber
  @Schema(description = "Phone number", example = "0392211343")
  private String phoneNumber;

  public String getUsername() {
    return phoneNumber;
  }

  @Password
  @Schema(description = "Password", example = "Admin123")
  private String password;
}

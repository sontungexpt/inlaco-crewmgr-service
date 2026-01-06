package com.inlaco.crewmgrservice.feature.auth.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest implements Serializable {

  @Schema(
      description = "Email or PhoneNumber",
      example = "admin@gmail.com",
      examples = {"admin@gmail.com", "0392211343"})
  @JsonAlias({"email", "phoneNumber"})
  @NotBlank
  private String username;

  public String getUsername() {
    return username;
  }

  @Schema(description = "Password", example = "Admin123")
  @NotBlank
  private String password;
}

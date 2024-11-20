package com.inlaco.crewmgrservice.feature.auth.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.inlaco.crewmgrservice.common.payload.IMatchPassword;
import com.inlaco.crewmgrservice.validation.annotation.OptimizedName;
import com.inlaco.crewmgrservice.validation.annotation.Password;
import com.inlaco.crewmgrservice.validation.annotation.PhoneNumber;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistrationRequest implements Serializable, IMatchPassword {

  @PhoneNumber
  @JsonAlias("username")
  @Schema(description = "Username", example = "+840392211343")
  private String phoneNumber;

  @Schema(hidden = true)
  public String getUsername() {
    return phoneNumber;
  }

  @Schema(description = "Password", example = "Admin123")
  @Password
  private String password;

  @Schema(description = "The confirm passowrd", example = "Admin123")
  @Password
  private String confirmPassowrd;

  @Schema(description = "Name", example = "Admin")
  @OptimizedName
  private String name;

  @Email
  @Schema(description = "Email", example = "admin@gmail.com")
  private String email;

  @Override
  public String getPasswordToMatch() {
    return password;
  }

  @Override
  public String getMatchingPassword() {
    return confirmPassowrd;
  }
}

package com.inlaco.crewmgrservice.feature.auth.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.inlaco.crewmgrservice.feature.user.enums.UsernameType;
import com.inlaco.crewmgrservice.infrastructure.web.payload.request.constraint.IMatchPassword;
import com.inlaco.crewmgrservice.infrastructure.web.validation.annotation.OptimizedName;
import com.inlaco.crewmgrservice.infrastructure.web.validation.password.Password;
import com.inlaco.crewmgrservice.infrastructure.web.validation.username.Username;
import com.inlaco.crewmgrservice.utils.PhoneNumberValidatorUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistrationRequest implements Serializable, IMatchPassword {

  @JsonAlias({"phoneNumber", "email"})
  @Schema(
      example = "tunggitclone03@gmail.com",
      description = "Username (Phone number or email)",
      examples = {"tunggitclone03@gmail.com", "+840392211343"})
  @Username
  private String username;

  @Schema(hidden = true)
  public UsernameType getUsernameType() {
    if (PhoneNumberValidatorUtils.isPotentialPhoneNumber(username)) {
      return UsernameType.PHONE_NUMBER;
    } else {
      return UsernameType.EMAIL;
    }
  }

  public String getUsername() {
    return username;
  }

  @Schema(description = "Password", example = "Admin123")
  @Password
  private String password;

  @Schema(description = "The confirm passowrd", example = "Admin123")
  @Password
  private String confirmPassword;

  @Schema(description = "Name", example = "Admin")
  @OptimizedName
  private String name;

  @Override
  @Schema(hidden = true)
  public String getPasswordToMatch() {
    return password;
  }

  @Override
  @Schema(hidden = true)
  public String getMatchingPassword() {
    return confirmPassword;
  }
}

package com.inlaco.crewmgrservice.feature.auth.presentation.dto.request;

import com.inlaco.crewmgrservice.infrastructure.web.payload.request.constraint.IMatchPassword;
import com.inlaco.crewmgrservice.infrastructure.web.validation.annotation.OptimizedName;
import com.inlaco.crewmgrservice.infrastructure.web.validation.password.Password;
import com.inlaco.crewmgrservice.infrastructure.web.validation.username.Username;
import java.io.Serializable;

public record RegistrationRequest(
    @Username String username,
    @Password String password,
    String confirmPassword,
    @OptimizedName String name)
    implements Serializable, IMatchPassword {

  @Override
  public String getPasswordToMatch() {
    return password;
  }

  @Override
  public String getMatchingPassword() {
    return confirmPassword;
  }
}

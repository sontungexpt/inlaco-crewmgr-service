package com.inlaco.crewmgrservice.feature.auth.presentation.dto.request;

import com.inlaco.crewmgrservice.infrastructure.web.payload.request.constraint.IDiffPassword;
import com.inlaco.crewmgrservice.infrastructure.web.payload.request.constraint.IMatchPassword;
import com.inlaco.crewmgrservice.infrastructure.web.validation.password.StrongPassword;
import jakarta.validation.constraints.NotBlank;

public record NewPasswordRequest(
    @NotBlank String oldPassword,
    @StrongPassword String newPassword,
    @NotBlank String confirmNewPassword)
    implements IMatchPassword, IDiffPassword {

  @Override
  public String getPasswordToMatch() {
    return newPassword;
  }

  @Override
  public String getMatchingPassword() {
    return confirmNewPassword;
  }

  @Override
  public String getPasswordToDiff() {
    return oldPassword;
  }

  @Override
  public String getDiffTargetPassword() {
    return newPassword;
  }
}

package com.inlaco.crewmgrservice.feature.auth.dto;

import com.inlaco.crewmgrservice.infrastructure.web.payload.request.constraint.IDiffPassword;
import com.inlaco.crewmgrservice.infrastructure.web.payload.request.constraint.IMatchPassword;
import com.inlaco.crewmgrservice.infrastructure.web.validation.password.Password;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NewPasswordRequest implements IMatchPassword, IDiffPassword {

  @Password private String oldPassword;

  @Password private String newPassword;

  @Password private String confirmNewPassword;

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

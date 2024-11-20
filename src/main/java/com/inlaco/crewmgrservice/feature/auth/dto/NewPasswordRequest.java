package com.inlaco.crewmgrservice.feature.auth.dto;

import com.inlaco.crewmgrservice.common.payload.IDiffPassword;
import com.inlaco.crewmgrservice.common.payload.IMatchPassword;
import com.inlaco.crewmgrservice.validation.annotation.Password;
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

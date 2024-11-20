package com.inlaco.crewmgrservice.common.payload;

import com.inlaco.crewmgrservice.validation.annotation.DiffPassword;
import jakarta.validation.Valid;

@Valid
@DiffPassword
public interface IDiffPassword {

  /** Gets the base password to be compared for differences. */
  String getPasswordToDiff();

  /** Gets the password that must be different. */
  String getDiffTargetPassword();
}

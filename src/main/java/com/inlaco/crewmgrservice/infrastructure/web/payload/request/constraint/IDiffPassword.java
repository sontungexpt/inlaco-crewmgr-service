package com.inlaco.crewmgrservice.infrastructure.web.payload.request.constraint;

import com.inlaco.crewmgrservice.infrastructure.web.validation.diffpassword.DiffPassword;
import jakarta.validation.Valid;

@Valid
@DiffPassword
public interface IDiffPassword {

  /** Gets the base password to be compared for differences. */
  String getPasswordToDiff();

  /** Gets the password that must be different. */
  String getDiffTargetPassword();
}

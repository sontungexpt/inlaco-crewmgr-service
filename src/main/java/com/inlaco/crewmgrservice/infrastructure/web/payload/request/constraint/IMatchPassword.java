package com.inlaco.crewmgrservice.infrastructure.web.payload.request.constraint;

import com.inlaco.crewmgrservice.infrastructure.web.validation.matchpassword.MatchPassword;
import jakarta.validation.Valid;

@Valid
@MatchPassword
public interface IMatchPassword {

  /** Gets the password that needs to be matched. */
  String getPasswordToMatch();

  /** Gets the password that must match. */
  String getMatchingPassword();
}

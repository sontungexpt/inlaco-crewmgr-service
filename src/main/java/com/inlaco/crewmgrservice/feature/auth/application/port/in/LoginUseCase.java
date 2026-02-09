package com.inlaco.crewmgrservice.feature.auth.application.port.in;

import com.inlaco.crewmgrservice.feature.auth.application.model.command.LoginCommand;
import com.inlaco.crewmgrservice.feature.auth.application.model.result.AuthTokenResult;

public interface LoginUseCase {
  AuthTokenResult login(LoginCommand command);
}

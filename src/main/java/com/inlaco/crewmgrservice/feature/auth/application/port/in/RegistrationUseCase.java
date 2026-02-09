package com.inlaco.crewmgrservice.feature.auth.application.port.in;

import com.inlaco.crewmgrservice.feature.auth.application.model.command.RegisterCommand;
import com.inlaco.crewmgrservice.feature.auth.application.model.result.AuthTokenResult;

public interface RegistrationUseCase {
  AuthTokenResult register(RegisterCommand command);
}

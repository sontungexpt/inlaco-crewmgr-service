package com.inlaco.crewmgrservice.feature.auth.application.service;

import com.inlaco.crewmgrservice.feature.auth.application.enums.VerificationPolicy;
import com.inlaco.crewmgrservice.feature.auth.application.model.command.RegisterCommand;
import com.inlaco.crewmgrservice.feature.auth.application.model.result.AuthTokenResult;
import com.inlaco.crewmgrservice.feature.auth.application.port.in.AccessTokenGenerator;
import com.inlaco.crewmgrservice.feature.auth.application.port.in.RefreshTokenManager;
import com.inlaco.crewmgrservice.feature.auth.application.port.in.RegistrationUseCase;
import com.inlaco.crewmgrservice.feature.user.application.port.in.UserUseCase;
import com.inlaco.crewmgrservice.feature.user.application.port.model.CreateUserCommand;
import com.inlaco.crewmgrservice.feature.user.domain.model.User;
import com.inlaco.crewmgrservice.shared.kernel.exception.ResourceAlreadyInUseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RegistrationService implements RegistrationUseCase {

  private final UserUseCase userUseCase;
  private final TwoStepVerificationDispatcher twoStepVerificationDispatcher;
  private final AccessTokenGenerator accessTokenGenerator;
  private final RefreshTokenManager refreshTokenManager;

  @Override
  public AuthTokenResult register(RegisterCommand command) {

    if (userUseCase.existsByUsername(command.username())) {
      throw new ResourceAlreadyInUseException(User.class, "username", command.username());
    }

    User newUser =
        userUseCase.create(
            new CreateUserCommand(command.username(), command.password(), command.name()));

    sendVerificationIfNeeded(newUser);

    return generateTokens(newUser.getPubId());
  }

  private void sendVerificationIfNeeded(User user) {
    switch (user.getUsernameType()) {
      case EMAIL -> twoStepVerificationDispatcher.send(VerificationPolicy.EMAIL, user);
      case PHONE_NUMBER -> {}
    }
  }

  private AuthTokenResult generateTokens(String pubId) {
    return new AuthTokenResult(
        accessTokenGenerator.generate(pubId), refreshTokenManager.issue(pubId));
  }
}

package com.inlaco.crewmgrservice.feature.auth.application.service;

import com.inlaco.crewmgrservice.application.exception.ResourceAlreadyInUseException;
import com.inlaco.crewmgrservice.application.exception.ResourceNotFoundException;
import com.inlaco.crewmgrservice.feature.auth.application.enums.VerificationPolicy;
import com.inlaco.crewmgrservice.feature.auth.application.model.command.RegisterCommand;
import com.inlaco.crewmgrservice.feature.auth.application.model.result.AuthTokenResult;
import com.inlaco.crewmgrservice.feature.auth.application.port.in.AccessTokenGenerator;
import com.inlaco.crewmgrservice.feature.auth.application.port.in.RefreshTokenManager;
import com.inlaco.crewmgrservice.feature.auth.application.port.in.RegistrationUseCase;
import com.inlaco.crewmgrservice.feature.auth.domain.model.RefreshToken;
import com.inlaco.crewmgrservice.feature.user.application.port.in.UserService;
import com.inlaco.crewmgrservice.feature.user.domain.enums.UsernameType;
import com.inlaco.crewmgrservice.feature.user.domain.model.User;
import com.inlaco.crewmgrservice.feature.user.domain.model.authorization.Right;
import com.inlaco.crewmgrservice.feature.user.domain.model.authorization.Role;
import com.inlaco.crewmgrservice.feature.user.infrastructure.persistence.mongodb.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RegistrationService implements RegistrationUseCase {

  private final UserService userService;
  private final RoleRepository roleRepository;
  private final TwoStepVerificationDispatcher twoStepVerificationDispatcher;
  private final PasswordEncoder passwordEncoder;
  private final AccessTokenGenerator accessTokenGenerator;
  private final RefreshTokenManager refreshTokenManager;

  @Override
  public AuthTokenResult register(RegisterCommand command) {
    String username = command.username();

    if (userService.existsByUsername(username)) {
      log.debug("User with username {} already exists", username);
      throw new ResourceAlreadyInUseException(User.class, "username", username);
    }

    log.debug("Starting registration for user {}", username);

    Role role =
        roleRepository
            .findByName("USER")
            .orElseThrow(() -> new ResourceNotFoundException(Role.class, "name", "ROLE_USER"));

    User user =
        userService.saveUser(
            new User(
                username,
                passwordEncoder.encode(command.password()),
                new Right(role),
                command.name()));

    var usernameType = user.getUsernameType();

    if (usernameType == UsernameType.EMAIL) {
      twoStepVerificationDispatcher.send(VerificationPolicy.EMAIL, user);
    } else if (usernameType == UsernameType.PHONE_NUMBER) {

    }

    final String accessToken = accessTokenGenerator.generate(user.getPubId());
    final RefreshToken refreshToken = refreshTokenManager.generate(user.getId());
    refreshTokenManager.save(refreshToken);

    log.debug("Account with public id {} logged in successfully", user.getPubId());

    return new AuthTokenResult(accessToken, refreshToken.getToken());
  }
}

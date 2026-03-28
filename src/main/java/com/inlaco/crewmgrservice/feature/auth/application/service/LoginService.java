package com.inlaco.crewmgrservice.feature.auth.application.service;

import com.inlaco.crewmgrservice.feature.auth.application.model.command.LoginCommand;
import com.inlaco.crewmgrservice.feature.auth.application.model.result.AuthTokenResult;
import com.inlaco.crewmgrservice.feature.auth.application.port.in.AccessTokenGenerator;
import com.inlaco.crewmgrservice.feature.auth.application.port.in.LoginUseCase;
import com.inlaco.crewmgrservice.feature.auth.application.port.in.RefreshTokenManager;
import com.inlaco.crewmgrservice.feature.auth.infrastructure.security.SecurityUser;
import com.inlaco.crewmgrservice.feature.user.domain.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoginService implements LoginUseCase {

  private final RefreshTokenManager refreshTokenManager;
  private final AccessTokenGenerator accessTokenGenerator;
  private final AuthenticationManager authenticationManager;

  @Override
  public AuthTokenResult login(LoginCommand command) {
    log.debug("Login attempt for user {}", command.username());
    Authentication authentication =
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(command.username(), command.password()));

    SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
    User user = securityUser.user();

    SecurityContextHolder.getContext().setAuthentication(authentication);

    String userPubId = user.getPubId();

    final String newAccessToken = accessTokenGenerator.generate(userPubId);
    final String newRefreshToken = refreshTokenManager.issue(userPubId);

    log.debug("Account with public id {} logged in successfully", userPubId);

    return new AuthTokenResult(newAccessToken, newRefreshToken);
  }
}

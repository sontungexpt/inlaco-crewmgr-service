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

/**
 * Service implementation for handling user authentication and login operations.
 *
 * <p>This service manages the authentication process, validating user credentials and generating
 * authentication tokens for successful logins. It integrates with Spring Security for credential
 * validation and manages security context.
 *
 * <p>The service generates both access tokens and refresh tokens, providing a complete
 * authentication solution that supports token-based authentication with proper session management.
 *
 * @author Trần Võ Sơn Tùng
 * @version 1.0
 * @since 1.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class LoginService implements LoginUseCase {

  private final RefreshTokenManager refreshTokenManager;
  private final AccessTokenGenerator accessTokenGenerator;
  private final AuthenticationManager authenticationManager;

  /**
   * Authenticates a user and generates authentication tokens.
   *
   * <p>This method validates user credentials using Spring Security's AuthenticationManager, and
   * upon successful authentication, generates both access and refresh tokens. The security context
   * is updated with the authenticated user for subsequent requests.
   *
   * <p>The method logs authentication attempts and successful logins for security monitoring and
   * audit purposes.
   *
   * @param command contains username and password for authentication
   * @return AuthTokenResult containing access and refresh tokens
   * @throws AuthenticationException if credentials are invalid
   */
  @Override
  public AuthTokenResult login(LoginCommand command) {
    log.info("Login attempt for user {}", command.username());
    Authentication authentication =
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(command.username(), command.password()));

    SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
    User user = securityUser.user();

    SecurityContextHolder.getContext().setAuthentication(authentication);

    String userPubId = user.getPubId();

    final String newAccessToken = accessTokenGenerator.generate(userPubId);
    final String newRefreshToken = refreshTokenManager.issue(userPubId);

    log.info("Account with public id {} logged in successfully", userPubId);

    return new AuthTokenResult(newAccessToken, newRefreshToken);
  }
}

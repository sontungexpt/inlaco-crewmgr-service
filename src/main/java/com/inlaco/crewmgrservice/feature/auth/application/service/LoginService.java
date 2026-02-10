package com.inlaco.crewmgrservice.feature.auth.application.service;

import com.inlaco.crewmgrservice.feature.auth.application.model.command.LoginCommand;
import com.inlaco.crewmgrservice.feature.auth.application.model.result.AuthTokenResult;
import com.inlaco.crewmgrservice.feature.auth.application.port.in.AccessTokenGenerator;
import com.inlaco.crewmgrservice.feature.auth.application.port.in.LoginUseCase;
import com.inlaco.crewmgrservice.feature.auth.application.port.in.RefreshTokenManager;
import com.inlaco.crewmgrservice.feature.auth.domain.model.RefreshToken;
import com.inlaco.crewmgrservice.feature.auth.infrastructure.security.SecurityUser;
import com.inlaco.crewmgrservice.feature.user.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoginService implements LoginUseCase {

  private final RefreshTokenManager refreshTokenService;
  private final AccessTokenGenerator accessTokenGenerator;
  private final AuthenticationManager authenticationManager;

  @Override
  public AuthTokenResult login(LoginCommand command) {
    Authentication authentication =
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(command.username(), command.password()));

    SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
    checkUserValid(securityUser);
    User user = securityUser.user();

    SecurityContextHolder.getContext().setAuthentication(authentication);

    final String accessToken = accessTokenGenerator.generate(user.getPubId());
    final RefreshToken refreshToken = refreshTokenService.generate(user.getId());
    refreshTokenService.save(refreshToken);

    log.debug("Account with public id {} logged in successfully", user.getPubId());

    return new AuthTokenResult(accessToken, refreshToken.getToken());
  }

  public void checkUserValid(UserDetails user) {
    if (!user.isAccountNonLocked()) {
      log.debug("Failed to authenticate since user account is locked");
      throw new LockedException(
          "AbstractUserDetailsAuthenticationProvider.locked. User account is locked");
    } else if (!user.isEnabled()) {
      log.debug("Failed to authenticate since user account is disabled");
      throw new DisabledException(
          "AbstractUserDetailsAuthenticationProvider.disabled. User is disabled");
    } else if (!user.isAccountNonExpired()) {
      log.debug("Failed to authenticate since user account has expired");
      throw new AccountExpiredException(
          "AbstractUserDetailsAuthenticationProvider.expired. User account has expired");
    } else if (!user.isCredentialsNonExpired()) {
      log.debug("Failed to authenticate since user credentials have expired");
      throw new AccountExpiredException(
          "AbstractUserDetailsAuthenticationProvider.expired. User credentials have expired");
    }
  }
}

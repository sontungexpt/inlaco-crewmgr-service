package com.inlaco.crewmgrservice.feature.auth.service.impl;

import com.inlaco.crewmgrservice.exceptions.ResourceAlreadyInUseException;
import com.inlaco.crewmgrservice.exceptions.ResourceNotFoundException;
import com.inlaco.crewmgrservice.feature.auth.dto.JwtResponse;
import com.inlaco.crewmgrservice.feature.auth.dto.LoginRequest;
import com.inlaco.crewmgrservice.feature.auth.dto.LoginResponse;
import com.inlaco.crewmgrservice.feature.auth.dto.RegistrationRequest;
import com.inlaco.crewmgrservice.feature.auth.enums.TwoStepVerificationType;
import com.inlaco.crewmgrservice.feature.auth.jwt.JwtService;
import com.inlaco.crewmgrservice.feature.auth.model.RefreshToken;
import com.inlaco.crewmgrservice.feature.auth.repository.RefreshTokenRepository;
import com.inlaco.crewmgrservice.feature.auth.service.AuthService;
import com.inlaco.crewmgrservice.feature.auth.service.RefreshTokenService;
import com.inlaco.crewmgrservice.feature.notify.NotificationFactory;
import com.inlaco.crewmgrservice.feature.user.enums.UsernameType;
import com.inlaco.crewmgrservice.feature.user.model.User;
import com.inlaco.crewmgrservice.feature.user.model.authorization.Right;
import com.inlaco.crewmgrservice.feature.user.model.authorization.Role;
import com.inlaco.crewmgrservice.feature.user.repository.RoleRepository;
import com.inlaco.crewmgrservice.feature.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public record AuthServiceImpl(
    RefreshTokenRepository refreshTokenRepository,
    RefreshTokenService refreshTokenService,
    JwtService jwtService,
    UserService userService,
    AuthenticationManager authenticationManager,
    PasswordEncoder passwordEncoder,
    NotificationFactory notificationFactory,
    TwoStepVerificationFactory twoStepVerificationFactory,
    RoleRepository roleRepository)
    implements AuthService {

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

  @Override
  public LoginResponse login(LoginRequest loginRequest) {
    Authentication authentication =
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(), loginRequest.getPassword()));
    User user = (User) authentication.getPrincipal();
    checkUserValid(user);

    SecurityContextHolder.getContext().setAuthentication(authentication);

    final String accessToken = jwtService.generateAccessToken(user);
    final RefreshToken refreshToken = jwtService.generateRefreshTokenAndSaveToDB(user);

    log.info("Account with public id {} logged in successfully", user.getPubId());

    return LoginResponse.builder()
        .name(user.getName())
        .jwt(new JwtResponse(accessToken, refreshToken.getToken()))
        .roles(user.getRight().getRoles().stream().map(Role::getName).toList())
        .build();
  }

  @Override
  @Transactional
  public void register(RegistrationRequest request) {
    final String username = request.getUsername();
    if (userService.existsByUsername(username)) {
      throw new ResourceAlreadyInUseException(User.class, "username", username);
    }

    log.info("Starting register new user");

    var usernameType = request.getUsernameType();

    Role role =
        roleRepository
            .findByName("USER")
            .orElseThrow(() -> new ResourceNotFoundException(Role.class, "name", "ROLE_USER"));

    User user =
        userService.saveUser(
            User.builder()
                .username(username)
                .usernameType(usernameType)
                .password(passwordEncoder.encode(request.getPassword()))
                .right(new Right(role))
                .name(request.getName())
                .build());

    if (usernameType == UsernameType.EMAIL) {
      twoStepVerificationFactory.sendVerificationCode(TwoStepVerificationType.EMAIL, user);
    } else if (usernameType == UsernameType.PHONE_NUMBER) {

    }
  }

  @Override
  @Transactional
  public JwtResponse refreshToken(String refreshToken) {
    RefreshToken savedRefreshToken =
        refreshTokenService.getAndValidateRefreshToken(
            refreshToken, this::handleRefreshtokenIntrusion);
    return refreshTokenService.refreshJwtTokens(savedRefreshToken);
  }

  public void handleRefreshtokenIntrusion(RefreshToken refreshToken) {
    log.warn("Infiltration detected");
  }

  @Override
  public void resend2StepVerification(String identifier) {
    User user = userService.findUserByUsername(identifier);
    twoStepVerificationFactory.resendVerificationCode(TwoStepVerificationType.EMAIL, user);
  }

  @Override
  public void verify2StepVerifiction(String token) {
    twoStepVerificationFactory.verifyCode(TwoStepVerificationType.EMAIL, token);
  }
}

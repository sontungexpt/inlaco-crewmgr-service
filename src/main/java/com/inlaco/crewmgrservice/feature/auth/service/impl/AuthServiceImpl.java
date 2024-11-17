package com.inlaco.crewmgrservice.feature.auth.service.impl;

import com.inlaco.crewmgrservice.exceptions.JwtTokenException;
import com.inlaco.crewmgrservice.exceptions.ResourceAlreadyInUseException;
import com.inlaco.crewmgrservice.feature.auth.dto.JwtResponse;
import com.inlaco.crewmgrservice.feature.auth.dto.LoginRequest;
import com.inlaco.crewmgrservice.feature.auth.dto.LoginResponse;
import com.inlaco.crewmgrservice.feature.auth.dto.RegistrationRequest;
import com.inlaco.crewmgrservice.feature.auth.jwt.JwtService;
import com.inlaco.crewmgrservice.feature.auth.model.RefreshToken;
import com.inlaco.crewmgrservice.feature.auth.repository.RefreshTokenRepository;
import com.inlaco.crewmgrservice.feature.auth.service.AuthService;
import com.inlaco.crewmgrservice.feature.user.model.User;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public record AuthServiceImpl(
    RefreshTokenRepository refreshTokenRepository,
    JwtService jwtService,
    UserService userService,
    AuthenticationManager authenticationManager)
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
        .build();
  }

  @Override
  @Transactional
  public void register(RegistrationRequest request) {
    final String phoneNumber = request.getPhoneNumber();
    if (userService.existsByPhoneNumber(phoneNumber)) {
      throw new ResourceAlreadyInUseException(User.class, "phoneNumber", phoneNumber);
    }

    // User user = userService.save(userService.createBasicUser(request));
    // OTPProperties otpProperties =
    //     OTPProperties.builder()
    //         .notificationType(NotificationType.SMS)
    //         .otpExpiration(OTPExpiration.SHORT)
    //         .otpType(OTPType.USER_REGISTRATION)
    //         .build();

    // otpService.send(user.getPhoneNumber(), otpProperties);

    // applicationEventPulisher.publishEvent(new UserWaitingOTPValidationEvent(this, user));
  }

  @Override
  @Transactional
  public JwtResponse refreshToken(String refreshToken) {
    RefreshToken savedRefreshToken =
        refreshTokenRepository
            .findByToken(refreshToken)
            .orElseThrow(
                () -> {
                  log.warn("Refresh token {} not found", refreshToken);
                  return new JwtTokenException(refreshToken, "Refresh token not found");
                });

    if (savedRefreshToken.isRevoked()) {
      handleRefreshtokenIntrusion(savedRefreshToken);
      throw new JwtTokenException(refreshToken, "Refresh token revoked");
    } else if (savedRefreshToken.isExpired()) {
      savedRefreshToken.revoke(refreshTokenRepository);
      throw new JwtTokenException(refreshToken, "Refresh token expired");
    }

    String userPubId = savedRefreshToken.getUserPubId();
    String newAccessToken = jwtService.generateAccessToken(userPubId);

    RefreshToken newRefreshToken = savedRefreshToken.refresh(refreshTokenRepository);

    log.info(
        "Refresh token {} refreshed successfully for user with public id {}",
        refreshToken,
        savedRefreshToken.getUserPubId());

    return new JwtResponse(newAccessToken, newRefreshToken.getToken());
  }

  public void handleRefreshtokenIntrusion(RefreshToken refreshToken) {
    log.warn("Infiltration detected");
  }
}

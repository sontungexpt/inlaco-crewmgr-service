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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

  @Override
  public LoginResponse login(LoginRequest loginRequest) {
    Authentication authentication =
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                loginRequest.getPhoneNumber(), loginRequest.getPassword()));

    User user = (User) authentication.getPrincipal();
    // if (user == null)
    //   throw new UsernameNotFoundException(
    //       "User not found with phone number " + loginRequest.getPhoneNumber());

    SecurityContextHolder.getContext().setAuthentication(authentication);

    final String accessToken = jwtService.generateAccessToken(user);
    final RefreshToken refreshToken = jwtService.generateRefreshToken(user);

    refreshTokenRepository.save(refreshToken);

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
      throw new JwtTokenException(refreshToken, "Refresh token revoked");
    } else if (savedRefreshToken.isExpired()) {
      refreshTokenRepository.save(savedRefreshToken.revoke());
      throw new JwtTokenException(refreshToken, "Refresh token expired");
    }

    String newAccessToken = jwtService.generateAccessToken(savedRefreshToken.getUserPubId());
    RefreshToken updatedRefreshToken = refreshTokenRepository.save(savedRefreshToken.refresh());

    log.info(
        "Refresh token {} refreshed successfully for user with public id {}",
        refreshToken,
        savedRefreshToken.getUserPubId());

    return new JwtResponse(newAccessToken, updatedRefreshToken.getToken());
  }
}

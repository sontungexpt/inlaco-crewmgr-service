package com.inlaco.crewmgrservice.feature.user.service.impl;

import com.inlaco.crewmgrservice.exceptions.ResourceNotFoundException;
import com.inlaco.crewmgrservice.feature.auth.dto.JwtResponse;
import com.inlaco.crewmgrservice.feature.auth.dto.NewPasswordRequest;
import com.inlaco.crewmgrservice.feature.auth.model.RefreshToken;
import com.inlaco.crewmgrservice.feature.auth.service.RefreshTokenService;
import com.inlaco.crewmgrservice.feature.user.model.User;
import com.inlaco.crewmgrservice.feature.user.repository.UserRepository;
import com.inlaco.crewmgrservice.feature.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public record UserServiceImpl(
    RefreshTokenService refreshTokenService,
    UserRepository userRepository,
    UserDetailsPasswordService userDetailsPasswordService,
    PasswordEncoder passwordEncoder)
    implements UserService {

  @Override
  public boolean existsByPhoneNumber(String phoneNumber) {
    return userRepository.existsByPhoneNumber(phoneNumber);
  }

  @Override
  public User findUserByPhoneNumber(String phoneNumber) {
    User user =
        userRepository
            .findByPhoneNumber(phoneNumber)
            .orElseThrow(
                () -> new ResourceNotFoundException(User.class, "phoneNumber", phoneNumber));
    return user;
  }

  @Override
  public User findUserByPubId(String pubId) {
    return userRepository
        .findByPubId(pubId)
        .orElseThrow(() -> new ResourceNotFoundException(User.class, "pubId", pubId));
  }

  @Override
  public User updateUser(User user) {
    return userRepository.save(user);
  }

  @Override
  public JwtResponse changePassword(String refreshToken, NewPasswordRequest newPasswordRequest) {
    RefreshToken savedRefreshToken =
        refreshTokenService.getAndValidateRefreshToken(refreshToken, this::handleInstruction);
    User user = findUserByPubId(savedRefreshToken.getUserPubId());
    if (!passwordEncoder.matches(newPasswordRequest.getOldPassword(), user.getPassword())) {
      throw new IllegalArgumentException("Old password is incorrect");
    }
    userDetailsPasswordService.updatePassword(user, newPasswordRequest.getNewPassword());
    return refreshTokenService.refreshJwtTokens(savedRefreshToken);
  }

  public void handleInstruction(RefreshToken refreshToken) {
    System.out.println("Change the password for an account");
    System.out.println(
        "Change the password for an account\n\n**Usecase**:\n- UC_account-doi-mat-khau\n\n");
  }
}

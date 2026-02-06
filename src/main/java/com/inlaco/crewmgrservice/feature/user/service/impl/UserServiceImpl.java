package com.inlaco.crewmgrservice.feature.user.service.impl;

import com.inlaco.crewmgrservice.application.exception.ResourceNotFoundException;
import com.inlaco.crewmgrservice.feature.auth.dto.JwtResponse;
import com.inlaco.crewmgrservice.feature.auth.dto.NewPasswordRequest;
import com.inlaco.crewmgrservice.feature.auth.model.RefreshToken;
import com.inlaco.crewmgrservice.feature.auth.service.RefreshTokenService;
import com.inlaco.crewmgrservice.feature.user.dto.UserProfile;
import com.inlaco.crewmgrservice.feature.user.model.User;
import com.inlaco.crewmgrservice.feature.user.model.authorization.Role;
import com.inlaco.crewmgrservice.feature.user.repository.RoleRepository;
import com.inlaco.crewmgrservice.feature.user.repository.UserRepository;
import com.inlaco.crewmgrservice.feature.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public record UserServiceImpl(
    RefreshTokenService refreshTokenService,
    UserRepository userRepository,
    RoleRepository roleRepository,
    PasswordEncoder passwordEncoder)
    implements UserService {

  @Override
  public boolean existsByUsername(String username) {
    return userRepository.existsByUsername(username);
  }

  @Override
  public User findUserByUsername(String username) {
    User user =
        userRepository
            .findByUsername(username)
            .orElseThrow(() -> new ResourceNotFoundException(User.class, "username", username));
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

    user.setPassword(passwordEncoder.encode(newPasswordRequest.getNewPassword()));
    userRepository.save(user);

    // userDetailsPasswordService.updatePassword(user, newPasswordRequest.getNewPassword());
    return refreshTokenService.refreshJwtTokens(savedRefreshToken);
  }

  public void handleInstruction(RefreshToken refreshToken) {
    System.out.println("Change the password for an account");
    System.out.println(
        "Change the password for an account\n\n**Usecase**:\n- UC_account-doi-mat-khau\n\n");
  }

  @Override
  public User saveUser(User user) {
    return userRepository.save(user);
  }

  @Override
  public User findUserById(String userId) {
    return userRepository
        .findById(userId)
        .orElseThrow(() -> new ResourceNotFoundException(User.class, "id", userId));
  }

  @Override
  public User updateToSailor(String userId) {
    User user = findUserById(userId);
    Role sailorRole =
        roleRepository
            .findByName("SAILOR")
            .orElseThrow(() -> new ResourceNotFoundException(Role.class, "name", "SAILOR"));
    user.promoteJobState();
    user.getRight().addRole(sailorRole);
    return userRepository.save(user);
  }

  @Override
  public UserProfile getUserProfile(User currentUser) {
    return UserProfile.builder()
        .name(currentUser.getName())
        .avatar(currentUser.getAvatar())
        .roles(currentUser.getRight().getRoles().stream().map(Role::getName).toList())
        .build();
  }
}

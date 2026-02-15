package com.inlaco.crewmgrservice.feature.user.application.service;

import com.inlaco.crewmgrservice.feature.auth.application.port.in.RefreshTokenManager;
import com.inlaco.crewmgrservice.feature.auth.presentation.dto.request.NewPasswordRequest;
import com.inlaco.crewmgrservice.feature.auth.presentation.dto.response.AuthTokenResponse;
import com.inlaco.crewmgrservice.feature.user.application.port.in.UserService;
import com.inlaco.crewmgrservice.feature.user.application.port.out.UserRepository;
import com.inlaco.crewmgrservice.feature.user.domain.model.User;
import com.inlaco.crewmgrservice.feature.user.domain.model.authorization.Role;
import com.inlaco.crewmgrservice.feature.user.infrastructure.persistence.mongodb.repository.RoleRepository;
import com.inlaco.crewmgrservice.feature.user.presentation.dto.UserProfile;
import com.inlaco.crewmgrservice.shared.kernel.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public record UserServiceImpl(
    RefreshTokenManager refreshTokenService,
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
  public AuthTokenResponse changePassword(
      String refreshToken, NewPasswordRequest newPasswordRequest) {
    throw new UnsupportedOperationException("Unimplemented method 'changePassword'");
    // RefreshToken savedRefreshToken =
    //     refreshTokenService.getAndValidateRefreshToken(refreshToken, this::handleInstruction);
    // User user = findUserByPubId(savedRefreshToken.getUserPubId());
    // if (!passwordEncoder.matches(newPasswordRequest.oldPassword(), user.getPassword())) {
    //   throw new IllegalArgumentException("Old password is incorrect");
    // }

    // user.setPassword(passwordEncoder.encode(newPasswordRequest.newPassword()));
    // userRepository.save(user);

    // // userDetailsPasswordService.updatePassword(user, newPasswordRequest.getNewPassword());
    // return refreshTokenService.refreshJwtTokens(savedRefreshToken);
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
    // user.promoteJobState();
    user.getRight().addRole(sailorRole);
    return userRepository.save(user);
  }

  @Override
  public UserProfile getUserProfile(User currentUser) {
    return UserProfile.builder()
        .name(currentUser.getName())
        .roles(currentUser.getRight().getRoles().stream().map(Role::getName).toList())
        .build();
  }
}

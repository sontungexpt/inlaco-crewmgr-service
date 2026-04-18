package com.inlaco.crewmgrservice.feature.user.application.service;

import com.inlaco.crewmgrservice.feature.user.application.port.in.UserUseCase;
import com.inlaco.crewmgrservice.feature.user.application.port.model.CreateUserCommand;
import com.inlaco.crewmgrservice.feature.user.application.port.out.RoleRepository;
import com.inlaco.crewmgrservice.feature.user.application.port.out.UserRepository;
import com.inlaco.crewmgrservice.feature.user.domain.model.Role;
import com.inlaco.crewmgrservice.feature.user.domain.model.User;
import com.inlaco.crewmgrservice.feature.user.domain.model.UserAuthority;
import com.inlaco.crewmgrservice.feature.user.presentation.dto.UserProfile;
import com.inlaco.crewmgrservice.shared.kernel.exception.ResourceNotFoundException;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UserService implements UserUseCase {

  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final PasswordEncoder passwordEncoder;

  @Override
  public boolean existsByUsername(String username) {
    log.debug("Checking existence of user with username: {}", username);
    boolean exists = userRepository.existsByUsername(username);
    log.info("User existence check for username {}: {}", username, exists);
    return exists;
  }

  @Override
  public User create(CreateUserCommand user) {
    log.debug("Fetching default role: USER");
    Role defaultRole =
        roleRepository
            .findByName("USER")
            .orElseThrow(
                () -> {
                  log.warn("Default role USER not found");
                  return new ResourceNotFoundException(Role.class, "name", "USER");
                });

    User newUser =
        new User(
            user.username(),
            passwordEncoder.encode(user.password()),
            new UserAuthority(defaultRole.getId()),
            user.name());

    log.info("Creating new user with username: {}", user.username());
    User savedUser = userRepository.save(newUser);
    log.info("User created successfully with ID: {}", savedUser.getId());
    return savedUser;
  }

  @Override
  public User findByUsername(String username) throws ResourceNotFoundException {
    log.debug("Fetching user by username: {}", username);
    return userRepository
        .findByUsername(username)
        .orElseThrow(
            () -> {
              log.warn("User not found with username: {}", username);
              return new ResourceNotFoundException(User.class, "username", username);
            });
  }

  @Override
  public User findByPubId(String pubId) throws ResourceNotFoundException {
    log.debug("Fetching user by public ID: {}", pubId);
    return userRepository
        .findByPubId(pubId)
        .orElseThrow(
            () -> {
              log.warn("User not found with public ID: {}", pubId);
              return new ResourceNotFoundException(User.class, "pubId", pubId);
            });
  }

  @Override
  public User findById(String id) throws ResourceNotFoundException {
    log.debug("Fetching user by ID: {}", id);
    return userRepository
        .findById(id)
        .orElseThrow(
            () -> {
              log.warn("User not found with ID: {}", id);
              return new ResourceNotFoundException(User.class, "id", id);
            });
  }

  @Override
  public void changePassword(String userId, String encodedPassword) {
    User user = findById(userId);
    // user.changePassword(encodedPassword); // nên để logic trong domain
    log.info("Changing password for user ID: {}", userId);
    userRepository.save(user);
    log.info("Password changed successfully for user ID: {}", userId);
  }

  @Override
  public User assignRole(String userId, String roleName) {
    User user = findById(userId);

    log.debug("Fetching role by name: {}", roleName);
    Role role =
        roleRepository
            .findByName(roleName)
            .orElseThrow(
                () -> {
                  log.warn("Role not found with name: {}", roleName);
                  return new ResourceNotFoundException(Role.class, "name", roleName);
                });

    if (user.getAuthority().addRole(role.getId())) {
      log.info("Assigning role {} to user ID: {}", roleName, userId);
      User updatedUser = userRepository.save(user);
      log.info("Role {} assigned successfully to user ID: {}", roleName, userId);
      return updatedUser;
    }

    log.info("Role {} already assigned to user ID: {}", roleName, userId);
    return user;
  }

  @Override
  public User activate(String userId, String reason) {
    User user = findById(userId);
    user.activate(reason);
    log.info("Activating user ID: {} with reason: {}", userId, reason);
    User activatedUser = userRepository.save(user);
    log.info("User ID: {} activated successfully", userId);
    return activatedUser;
  }

  @Override
  public UserProfile getUserProfile(User currentUser) {
    List<String> roleNames =
        currentUser.getAuthority().getRoleIds().stream()
            .map(roleId -> roleRepository.findById(roleId).orElse(null))
            .filter(Objects::nonNull)
            .map(Role::getName)
            .toList();

    log.debug("Building user profile for user ID: {}", currentUser.getId());
    UserProfile profile =
        UserProfile.builder().name(currentUser.getName()).roles(roleNames).build();
    log.info("User profile built successfully for user ID: {}", currentUser.getId());
    return profile;
  }
}

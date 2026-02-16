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
    return userRepository.existsByUsername(username);
  }

  @Override
  public User create(CreateUserCommand user) {
    Role defaultRole =
        roleRepository
            .findByName("USER")
            .orElseThrow(() -> new ResourceNotFoundException(Role.class, "name", "USER"));

    User newUser =
        new User(
            user.username(),
            passwordEncoder.encode(user.password()),
            new UserAuthority(defaultRole.getId()),
            user.name());

    return userRepository.save(newUser);
  }

  @Override
  public User findByUsername(String username) throws ResourceNotFoundException {
    return userRepository
        .findByUsername(username)
        .orElseThrow(() -> new ResourceNotFoundException(User.class, "username", username));
  }

  @Override
  public User findByPubId(String pubId) throws ResourceNotFoundException {
    return userRepository
        .findByPubId(pubId)
        .orElseThrow(() -> new ResourceNotFoundException(User.class, "pubId", pubId));
  }

  @Override
  public User findById(String id) throws ResourceNotFoundException {
    return userRepository
        .findById(id)
        .orElseThrow(() -> new ResourceNotFoundException(User.class, "id", id));
  }

  @Override
  public void changePassword(String userId, String encodedPassword) {
    User user = findById(userId);
    // user.changePassword(encodedPassword); // nên để logic trong domain
    userRepository.save(user);
  }

  @Override
  public User assignRole(String userId, String roleName) {
    User user = findById(userId);

    Role role =
        roleRepository
            .findByName(roleName)
            .orElseThrow(() -> new ResourceNotFoundException(Role.class, "name", roleName));

    if (user.getAuthority().addRole(role.getId())) {
      return userRepository.save(user);
    }

    return user;
  }

  @Override
  public User activate(String userId, String reason) {
    User user = findById(userId);
    user.activate(reason);
    return userRepository.save(user);
  }

  @Override
  public UserProfile getUserProfile(User currentUser) {
    List<String> roleNames =
        currentUser.getAuthority().getRoleIds().stream()
            .map(roleId -> roleRepository.findById(roleId).orElse(null))
            .filter(Objects::nonNull)
            .map(Role::getName)
            .toList();

    return UserProfile.builder().name(currentUser.getName()).roles(roleNames).build();
  }
}

package com.inlaco.crewmgrservice.feature.user.application.port.in;

import com.inlaco.crewmgrservice.feature.user.application.port.model.CreateUserCommand;
import com.inlaco.crewmgrservice.feature.user.domain.model.User;
import com.inlaco.crewmgrservice.feature.user.presentation.dto.UserProfile;
import com.inlaco.crewmgrservice.shared.kernel.exception.ResourceNotFoundException;

public interface UserUseCase {

  boolean existsByUsername(String username);

  User create(CreateUserCommand user);

  User findById(String id) throws ResourceNotFoundException;

  User findByPubId(String pubId) throws ResourceNotFoundException;

  User findByUsername(String username) throws ResourceNotFoundException;

  void changePassword(String userId, String encodedPassword);

  User activate(String userId, String reason);

  User assignRole(String userId, String roleId);

  UserProfile getUserProfile(User currentUser);
}

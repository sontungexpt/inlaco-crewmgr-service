package com.inlaco.crewmgrservice.feature.user.service;

import com.inlaco.crewmgrservice.feature.auth.dto.JwtResponse;
import com.inlaco.crewmgrservice.feature.auth.dto.NewPasswordRequest;
import com.inlaco.crewmgrservice.feature.user.model.User;

public interface UserService {

  boolean existsByUsername(String username);

  User findUserByUsername(String username);

  User saveUser(User user);

  User updateUser(User user);

  User findUserByPubId(String pubId);

  User findUserById(String userId);

  User updateToSailor(String userId);

  JwtResponse changePassword(String refreshToken, NewPasswordRequest newPasswordRequest);
}

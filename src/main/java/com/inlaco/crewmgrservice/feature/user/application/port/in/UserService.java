package com.inlaco.crewmgrservice.feature.user.application.port.in;

import com.inlaco.crewmgrservice.feature.auth.presentation.dto.request.NewPasswordRequest;
import com.inlaco.crewmgrservice.feature.auth.presentation.dto.response.AuthTokenResponse;
import com.inlaco.crewmgrservice.feature.user.domain.model.User;
import com.inlaco.crewmgrservice.feature.user.presentation.dto.UserProfile;

public interface UserService {

  boolean existsByUsername(String username);

  User findUserByUsername(String username);

  User saveUser(User user);

  User updateUser(User user);

  User findUserByPubId(String pubId);

  User findUserById(String userId);

  User updateToSailor(String userId);

  AuthTokenResponse changePassword(String refreshToken, NewPasswordRequest newPasswordRequest);

  UserProfile getUserProfile(User currentUser);
}

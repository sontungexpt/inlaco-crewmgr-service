package com.inlaco.crewmgrservice.feature.user.service;

import com.inlaco.crewmgrservice.feature.auth.dto.JwtResponse;
import com.inlaco.crewmgrservice.feature.auth.dto.NewPasswordRequest;
import com.inlaco.crewmgrservice.feature.user.model.User;

public interface UserService {

  boolean existsByPhoneNumber(String phoneNumber);

  User findUserByPhoneNumber(String phoneNumber);

  User updateUser(User user);

  User findUserByPubId(String pubId);

  JwtResponse changePassword(String refreshToken, NewPasswordRequest newPasswordRequest);
}

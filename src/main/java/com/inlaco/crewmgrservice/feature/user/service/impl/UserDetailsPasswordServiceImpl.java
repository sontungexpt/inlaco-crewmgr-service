package com.inlaco.crewmgrservice.feature.user.service.impl;

import com.inlaco.crewmgrservice.feature.user.model.User;
import com.inlaco.crewmgrservice.feature.user.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.stereotype.Service;

@Service
public record UserDetailsPasswordServiceImpl(UserRepository userRepository)
    implements UserDetailsPasswordService {

  @Override
  public UserDetails updatePassword(UserDetails userDetails, String newPassword) {
    assert userDetails instanceof User;

    User user = (User) userDetails;
    user.setPassword(newPassword);
    userRepository.save(user);
    return user;
  }
}

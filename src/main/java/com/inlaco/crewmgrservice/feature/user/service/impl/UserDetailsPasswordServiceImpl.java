package com.inlaco.crewmgrservice.feature.user.service.impl;

import com.inlaco.crewmgrservice.feature.user.model.SecurityUser;
import com.inlaco.crewmgrservice.feature.user.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public record UserDetailsPasswordServiceImpl(
    UserRepository userRepository, PasswordEncoder passwordEncoder)
    implements UserDetailsPasswordService {

  @Override
  public UserDetails updatePassword(UserDetails userDetails, String newPassword) {
    SecurityUser securityUser = ((SecurityUser) userDetails);
    securityUser.getUser().setPassword(passwordEncoder.encode(newPassword));
    userRepository.save(securityUser.getUser());
    return securityUser;
  }
}

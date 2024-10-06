package com.inlaco.crewmgrservice.feature.user.service.impl;

import com.inlaco.crewmgrservice.feature.user.model.User;
import com.inlaco.crewmgrservice.feature.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public record UserDetailsServiceImpl(UserRepository userRepository) implements UserDetailsService {

  @Override
  public User loadUserByUsername(String username) throws UsernameNotFoundException {
    User account =
        userRepository
            .findByPhoneNumber(username)
            .orElseThrow(
                () -> new UsernameNotFoundException("User Not Found with username: " + username));

    log.info("Fetch account by phone number: {}", account);

    return account;
  }
}

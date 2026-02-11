package com.inlaco.crewmgrservice.feature.auth.infrastructure.security;

import com.inlaco.crewmgrservice.feature.user.application.port.out.UserRepository;
import com.inlaco.crewmgrservice.feature.user.domain.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public record UserDetailsServiceImpl(
    UserRepository userRepository, AuthorityResolver authorityResolver)
    implements UserDetailsService {

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    log.debug("Load user with username {}", username);
    User user =
        userRepository
            .findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException(username));
    return new SecurityUser(user, authorityResolver.resolve(user));
  }
}

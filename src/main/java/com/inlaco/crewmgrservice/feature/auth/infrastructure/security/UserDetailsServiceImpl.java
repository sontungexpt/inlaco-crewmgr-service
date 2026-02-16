package com.inlaco.crewmgrservice.feature.auth.infrastructure.security;

import com.inlaco.crewmgrservice.feature.user.application.port.in.UserUseCase;
import com.inlaco.crewmgrservice.feature.user.domain.model.User;
import com.inlaco.crewmgrservice.shared.kernel.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public record UserDetailsServiceImpl(UserUseCase userUseCase, AuthorityResolver authorityResolver)
    implements UserDetailsService {

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    try {
      log.debug("Load user with username {}", username);
      User user = userUseCase.findByUsername(username);
      return new SecurityUser(user, authorityResolver.resolve(user));
    } catch (ResourceNotFoundException e) {
      throw new UsernameNotFoundException("User with  username " + username + " not found");
    }
  }
}

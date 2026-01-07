package com.inlaco.crewmgrservice.feature.user.service;

import com.inlaco.crewmgrservice.feature.user.model.User;
import java.util.Set;
import org.springframework.security.core.GrantedAuthority;

public interface AuthorityResolver {
  Set<GrantedAuthority> resolve(User user);
}

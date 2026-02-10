package com.inlaco.crewmgrservice.feature.auth.infrastructure.security;

import com.inlaco.crewmgrservice.feature.user.model.User;
import java.util.Set;
import org.springframework.security.core.GrantedAuthority;

public interface AuthorityResolver {
  Set<? extends GrantedAuthority> resolve(User user);
}

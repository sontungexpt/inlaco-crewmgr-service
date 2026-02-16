package com.inlaco.crewmgrservice.feature.auth.infrastructure.security;

import com.inlaco.crewmgrservice.feature.user.application.port.out.RoleRepository;
import com.inlaco.crewmgrservice.feature.user.domain.model.Role;
import com.inlaco.crewmgrservice.feature.user.domain.model.User;
import com.inlaco.crewmgrservice.feature.user.domain.model.UserAuthority;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class DefaultAuthorityResolver implements AuthorityResolver {

  private final RoleRepository roleRepository;

  @Override
  public Set<GrantedAuthority> resolve(User user) {
    UserAuthority authority = user.getAuthority();
    if (authority == null) return Set.of();

    List<Role> roles = roleRepository.findAllById(authority.getRoleIds());

    // Build authorities
    Set<GrantedAuthority> auths = new HashSet<>(roles.size());

    for (Role role : roles) {
      // ROLE_ prefix for roles
      auths.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));

      // permissions (if role stores Permission objects)
      // if (role.getPermissions() != null) {
      //   for (EndpointPermission perm : role.getPermissions()) {
      //     if (perm == null) continue;

      //     // permission name as authority
      //     if (perm.getName() != null) {
      //       auths.add(new SimpleGrantedAuthority(perm.getName()));
      //     }

      //     // api endpoints as authorities
      //     if (perm.getApiEndpoints() != null) {
      //       for (APIEndpointName api : perm.getApiEndpoints()) {
      //         if (api != null) {
      //           auths.add(new SimpleGrantedAuthority(api.name()));
      //         }
      //       }
      //     }
      //   }
      // }
    }

    // Optionally add any included endpoints from UserAuthority if you re-enable them
    // user.getAuthority().getIncludedEndpoints()...

    return auths;
  }
}

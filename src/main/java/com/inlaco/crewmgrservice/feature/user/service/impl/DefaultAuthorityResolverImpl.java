package com.inlaco.crewmgrservice.feature.user.service.impl;

import com.inlaco.crewmgrservice.feature.user.model.User;
import com.inlaco.crewmgrservice.feature.user.service.AuthorityResolver;
import java.util.HashSet;
import java.util.Set;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

@Service
public class DefaultAuthorityResolverImpl implements AuthorityResolver {

  @Override
  public Set<GrantedAuthority> resolve(User user) {
    if (user.getRight() == null) return Set.of();

    Set<GrantedAuthority> auths = new HashSet<>();

    user.getRight()
        .getRoles()
        .forEach(
            role -> {
              auths.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));

              role.getPermissions()
                  .forEach(
                      permission -> {
                        auths.add(new SimpleGrantedAuthority(permission.getName()));

                        permission
                            .getApiEndpoints()
                            .forEach(
                                api -> {
                                  if (!user.getRight().getExcludedEndpoints().contains(api)) {
                                    auths.add(new SimpleGrantedAuthority(api.name()));
                                  }
                                });
                      });
            });

    user.getRight()
        .getIncludedEndpoints()
        .forEach(
            api -> {
              if (!user.getRight().getExcludedEndpoints().contains(api)) {
                auths.add(new SimpleGrantedAuthority(api.name()));
              }
            });

    return auths;
  }
}

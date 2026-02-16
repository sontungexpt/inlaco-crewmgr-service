package com.inlaco.crewmgrservice.feature.user.domain.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserAuthority {

  private Set<String> roleIds = new HashSet<>();

  // private Set<APIEndpointName> includedEndpoints = Set.of();

  // private Set<APIEndpointName> excludedEndpoints = Set.of();

  public UserAuthority(Collection<String> ids) {
    this.roleIds.addAll(ids);
  }

  public UserAuthority(String role) {
    roleIds.add(role);
  }

  public boolean addRole(String roleId) {
    return roleIds.add(roleId);
  }

  public boolean removeRole(String roleId) {
    return roleIds.remove(roleId);
  }
}

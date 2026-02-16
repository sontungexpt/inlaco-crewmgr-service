package com.inlaco.crewmgrservice.feature.user.domain.model.authorization;

import com.inlaco.crewmgrservice.endpoint.APIEndpointName;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.DBRef;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Right {

  @DBRef private Set<Role> roles = new HashSet<>();

  private Set<APIEndpointName> includedEndpoints = Set.of();

  private Set<APIEndpointName> excludedEndpoints = Set.of();

  public Right(Collection<Role> roles) {
    this.roles = new HashSet<>(roles);
  }

  public Right(Role role) {
    roles.add(role);
  }

  public boolean addRole(Role role) {
    return roles.add(role);
  }

  public boolean removeRole(Role role) {
    return roles.remove(role);
  }
}

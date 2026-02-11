package com.inlaco.crewmgrservice.feature.user.domain.model.authorization;

import com.inlaco.crewmgrservice.endpoint.APIEndpointName;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
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
@Schema(description = "The main entity for the authorization")
public class Right {

  @Schema(description = "Roles that this right has", requiredMode = RequiredMode.REQUIRED)
  @DBRef
  private Set<Role> roles = new HashSet<>();

  @Schema(description = "Included endpoints", requiredMode = RequiredMode.NOT_REQUIRED)
  private Set<APIEndpointName> includedEndpoints = Set.of();

  @Schema(description = "Excluded endpoints", requiredMode = RequiredMode.NOT_REQUIRED)
  private Set<APIEndpointName> excludedEndpoints = Set.of();

  public Right(Collection<Role> roles) {
    this.roles = new HashSet<>(roles);
  }

  public Right(Role role) {
    roles.add(role);
  }

  public Right addRole(Role role) {
    roles.add(role);
    return this;
  }

  public Right removeRole(Role role) {
    roles.remove(role);
    return this;
  }
}

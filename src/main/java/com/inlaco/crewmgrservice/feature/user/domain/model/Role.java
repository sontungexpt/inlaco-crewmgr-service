package com.inlaco.crewmgrservice.feature.user.domain.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = "id")
public class Role {

  private String id;

  private String name;

  private String description;

  // private Set<EndpointPermission> permissions = Set.of();

}

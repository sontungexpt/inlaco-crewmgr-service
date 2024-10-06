package com.inlaco.crewmgrservice.feature.user.model;

import com.inlaco.crewmgrservice.feature.user.model.authorization.Role;
import java.security.Permission;
import java.util.Set;

public class Right {

  private Set<Role> role;

  private Set<Permission> permission;
}

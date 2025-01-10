package com.inlaco.crewmgrservice.feature.user.service.impl.userRight;

import com.inlaco.crewmgrservice.feature.user.model.authorization.Role;
import com.inlaco.crewmgrservice.feature.user.repository.PermissionRepository;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DefaultRoleFactory {

  private final PermissionRepository permissionRepository;

  public static class Name {
    public static final String ADMIN = "ADMIN";
    public static final String SAILOR = "SAILOR";
    public static final String CANDIDATE = "CANDIDATE";
  }

  public Role createAdminRole() {

    return Role.builder()
        .name(Name.ADMIN)
        .version(1)
        .description("Admin role")
        .permissions(Set.of())
        .build();
  }
}

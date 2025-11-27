package com.inlaco.crewmgrservice.feature.user.service.impl.userRight;

import com.inlaco.crewmgrservice.feature.user.model.authorization.EndpointPermission;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DefaultPermissionFactory {

  // private final PermissionRepository permissionRepository;

  public EndpointPermission generateGetPostPermission() {

    return EndpointPermission.builder().name("").build();
  }
}

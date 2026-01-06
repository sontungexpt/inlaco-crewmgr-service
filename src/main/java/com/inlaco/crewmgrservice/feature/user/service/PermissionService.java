package com.inlaco.crewmgrservice.feature.user.service;

import com.inlaco.crewmgrservice.feature.user.model.authorization.EndpointPermission;
import java.util.List;
import org.springframework.data.domain.Page;

public interface PermissionService {

  Page<EndpointPermission> getPaginationPermissions();

  List<EndpointPermission> getPermissions();

  EndpointPermission getPermissionById(String id);

  EndpointPermission getPermissionByName(String name);

  EndpointPermission createPermission(EndpointPermission permission);

  EndpointPermission updatePermission(String id, EndpointPermission permission);

  void deletePermission(String id);
}

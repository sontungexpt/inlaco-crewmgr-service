package com.inlaco.crewmgrservice.feature.user.service;

import com.inlaco.crewmgrservice.feature.user.model.authorization.Permission;
import java.util.List;
import org.springframework.data.domain.Page;

public interface PermissionService {

  Page<Permission> getPaginationPermissions();

  List<Permission> getPermissions();

  Permission getPermissionById(String id);

  Permission getPermissionByName(String name);

  Permission createPermission(Permission permission);

  Permission updatePermission(String id, Permission permission);

  void deletePermission(String id);
}

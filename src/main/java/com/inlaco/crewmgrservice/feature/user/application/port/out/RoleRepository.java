package com.inlaco.crewmgrservice.feature.user.application.port.out;

import com.inlaco.crewmgrservice.feature.user.domain.model.Role;
import java.util.List;
import java.util.Optional;

public interface RoleRepository {

  Optional<Role> findById(String id);

  Optional<Role> findByName(String name);

  List<Role> findAllById(Iterable<String> ids);

  List<Role> findAll();

  Role save(Role role);

  boolean existsByName(String name);
}

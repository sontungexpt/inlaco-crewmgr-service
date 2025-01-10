package com.inlaco.crewmgrservice.feature.user.repository;

import com.inlaco.crewmgrservice.feature.user.model.authorization.Permission;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends MongoRepository<Permission, String> {

  boolean existsByName(String name);

  Permission findByName(String name);
}

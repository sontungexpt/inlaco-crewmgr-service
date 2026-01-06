package com.inlaco.crewmgrservice.feature.user.repository;

import com.inlaco.crewmgrservice.feature.user.model.authorization.EndpointPermission;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends MongoRepository<EndpointPermission, String> {

  boolean existsByName(String name);

  EndpointPermission findByName(String name);
}

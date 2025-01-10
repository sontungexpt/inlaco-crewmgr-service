package com.inlaco.crewmgrservice.feature.user.repository;

import com.inlaco.crewmgrservice.feature.user.model.authorization.Role;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends MongoRepository<Role, String> {
  boolean existsByName(String name);
}

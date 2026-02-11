package com.inlaco.crewmgrservice.feature.user.infrastructure.persistence.mongodb.repository;

import com.inlaco.crewmgrservice.feature.user.domain.model.authorization.Role;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends MongoRepository<Role, String> {
  boolean existsByName(String name);

  Optional<Role> findByName(String name);
}

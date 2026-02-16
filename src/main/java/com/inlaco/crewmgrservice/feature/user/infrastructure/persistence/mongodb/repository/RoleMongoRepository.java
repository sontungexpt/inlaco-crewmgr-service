package com.inlaco.crewmgrservice.feature.user.infrastructure.persistence.mongodb.repository;

import com.inlaco.crewmgrservice.feature.user.infrastructure.persistence.mongodb.entity.RoleEntity;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleMongoRepository extends MongoRepository<RoleEntity, String> {
  boolean existsByName(String name);

  Optional<RoleEntity> findByName(String name);
}

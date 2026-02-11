package com.inlaco.crewmgrservice.feature.user.infrastructure.persistence.mongodb.repository;

import com.inlaco.crewmgrservice.feature.user.infrastructure.persistence.mongodb.entity.UserEntity;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserEntityMongoRepository extends MongoRepository<UserEntity, String> {
  Optional<UserEntity> findByUsername(String username);

  Optional<UserEntity> findByPubId(String pubId);

  boolean existsByUsername(String username);
}

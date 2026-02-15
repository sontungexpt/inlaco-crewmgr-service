package com.inlaco.crewmgrservice.feature.auth.infrastructure.persistence.mongodb.repository;

import com.inlaco.crewmgrservice.feature.auth.infrastructure.persistence.mongodb.entity.RefreshTokenEntity;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenMongoRepository extends MongoRepository<RefreshTokenEntity, String> {
  Optional<RefreshTokenEntity> findByHashedToken(String token);
}

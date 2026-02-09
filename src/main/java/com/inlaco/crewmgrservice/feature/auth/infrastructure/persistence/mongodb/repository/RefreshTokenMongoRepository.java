package com.inlaco.crewmgrservice.feature.auth.infrastructure.persistence.mongodb.repository;

import com.inlaco.crewmgrservice.feature.auth.infrastructure.persistence.mongodb.entity.RefreshTokenEntity;
import java.util.Optional;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenMongoRepository extends MongoRepository<RefreshTokenEntity, ObjectId> {
  Optional<RefreshTokenEntity> findByToken(String token);

  int deleteByToken(String token);
}

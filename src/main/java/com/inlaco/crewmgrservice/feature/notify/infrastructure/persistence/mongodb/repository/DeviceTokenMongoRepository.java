package com.inlaco.crewmgrservice.feature.notify.infrastructure.persistence.mongodb.repository;

import com.inlaco.crewmgrservice.feature.notify.infrastructure.persistence.mongodb.entity.DeviceTokenEntity;
import java.util.List;
import java.util.Optional;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceTokenMongoRepository extends MongoRepository<DeviceTokenEntity, String> {
  Optional<DeviceTokenEntity> findByToken(String token);

  List<DeviceTokenEntity> findByUserId(ObjectId userId);

  void deleteByToken(String token);

  boolean existsByToken(String token);
}

package com.inlaco.crewmgrservice.feature.shipschedule.infrastructure.persistence.mongodb.repository;

import com.inlaco.crewmgrservice.feature.shipschedule.infrastructure.persistence.mongodb.entity.QRCodeEntity;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface QRCodeMongoRepository extends MongoRepository<QRCodeEntity, String> {
  Optional<QRCodeEntity> findByToken(String token);
  
  @Query("{ 'token': ?0, 'used': false }")
  Optional<QRCodeEntity> findByTokenAndNotUsed(String token);
}

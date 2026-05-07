package com.inlaco.crewmgrservice.feature.shipschedule.infrastructure.persistence.mongodb.repository;

import com.inlaco.crewmgrservice.feature.shipschedule.infrastructure.persistence.mongodb.entity.AttendanceQRCodeEntity;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AttendanceQRCodeMongoRepository
    extends MongoRepository<AttendanceQRCodeEntity, String> {
  Optional<AttendanceQRCodeEntity> findByToken(String token);

  @Query("{ 'token': ?0, 'used': false }")
  Optional<AttendanceQRCodeEntity> findByTokenAndNotUsed(String token);
}

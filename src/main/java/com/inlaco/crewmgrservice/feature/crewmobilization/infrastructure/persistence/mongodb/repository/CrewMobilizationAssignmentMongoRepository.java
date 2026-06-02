package com.inlaco.crewmgrservice.feature.crewmobilization.infrastructure.persistence.mongodb.repository;

import com.inlaco.crewmgrservice.feature.crewmobilization.infrastructure.persistence.mongodb.entity.CrewMobilizationAssignmentEntity;
import java.time.Instant;
import java.util.List;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CrewMobilizationAssignmentMongoRepository
    extends MongoRepository<CrewMobilizationAssignmentEntity, String> {
  List<CrewMobilizationAssignmentEntity> findByMobilizationId(ObjectId mobilizationId);

  List<CrewMobilizationAssignmentEntity> findByProfileId(ObjectId profileId);

  List<CrewMobilizationAssignmentEntity> findByEmployeeCardIdInAndEndDateGreaterThan(
      Iterable<String> employeeCardIds, Instant date);
}

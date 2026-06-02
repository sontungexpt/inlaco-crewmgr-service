package com.inlaco.crewmgrservice.feature.crewmobilization.infrastructure.persistence.mongodb.repository;

import com.inlaco.crewmgrservice.feature.crewmobilization.infrastructure.persistence.mongodb.entity.CrewMobilizationEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CrewMobilizationMongoRepository
    extends MongoRepository<CrewMobilizationEntity, String> {}

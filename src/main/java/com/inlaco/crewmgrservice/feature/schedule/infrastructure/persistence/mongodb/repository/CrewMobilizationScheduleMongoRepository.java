package com.inlaco.crewmgrservice.feature.schedule.infrastructure.persistence.mongodb.repository;

import com.inlaco.crewmgrservice.feature.schedule.infrastructure.persistence.mongodb.entity.CrewMobilizationScheduleEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CrewMobilizationScheduleMongoRepository
    extends MongoRepository<CrewMobilizationScheduleEntity, String> {}

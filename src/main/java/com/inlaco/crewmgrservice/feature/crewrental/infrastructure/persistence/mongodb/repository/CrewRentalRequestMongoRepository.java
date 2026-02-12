package com.inlaco.crewmgrservice.feature.crewrental.infrastructure.persistence.mongodb.repository;

import com.inlaco.crewmgrservice.feature.crewrental.infrastructure.persistence.mongodb.entity.CrewRentalRequestEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CrewRentalRequestMongoRepository
    extends MongoRepository<CrewRentalRequestEntity, String> {}

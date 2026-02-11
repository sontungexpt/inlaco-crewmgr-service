package com.inlaco.crewmgrservice.feature.recruitment.infrastructure.persistence.mongodb.repository;

import com.inlaco.crewmgrservice.feature.recruitment.infrastructure.persistence.mongodb.entity.JobApplicationEntity;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobApplicationMongoRepository
    extends MongoRepository<JobApplicationEntity, String> {

  Page<JobApplicationEntity> findByAccountId(ObjectId accountId, Pageable pageable);
}

package com.inlaco.crewmgrservice.feature.contract.infrastructure.persistence.repository;

import com.inlaco.crewmgrservice.feature.contract.infrastructure.persistence.entity.ContractEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContractMongoRepository extends MongoRepository<ContractEntity, String> {}

package com.inlaco.crewmgrservice.feature.contract.repository;

import com.inlaco.crewmgrservice.feature.contract.model.ContractVersion;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContractVersionRepository extends MongoRepository<ContractVersion, String> {}

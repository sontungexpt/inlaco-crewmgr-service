package com.inlaco.crewmgrservice.feature.contract.repository;

import com.inlaco.crewmgrservice.feature.contract.domain.model.AbstractContract;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContractRepository extends MongoRepository<AbstractContract, String> {

  Page<AbstractContract> findBySigned(boolean signed, Pageable pageable);

  Page<AbstractContract> findBySignedAndType(boolean signed, String type, Pageable pageable);
}

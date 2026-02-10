package com.inlaco.crewmgrservice.feature.contract.repository;

import com.inlaco.crewmgrservice.feature.contract.domain.model.ContractTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContractTemplateRepository extends MongoRepository<ContractTemplate, String> {

  Page<ContractTemplate> findByType(String type, Pageable pageable);
}

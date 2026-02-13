package com.inlaco.crewmgrservice.feature.contracttemplate.infrastructure.persistence.mongodb.repository;

import com.inlaco.crewmgrservice.feature.contracttemplate.infrastructure.persistence.mongodb.entity.ContractTemplateEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContractTemplateMongoRepository
    extends MongoRepository<ContractTemplateEntity, String> {

  Page<ContractTemplateEntity> findByType(String type, Pageable pageable);
}

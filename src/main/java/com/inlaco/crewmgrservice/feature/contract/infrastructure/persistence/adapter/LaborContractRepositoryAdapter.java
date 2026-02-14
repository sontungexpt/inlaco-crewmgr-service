package com.inlaco.crewmgrservice.feature.contract.infrastructure.persistence.adapter;

import com.inlaco.crewmgrservice.feature.contract.application.port.out.LaborContractRepository;
import com.inlaco.crewmgrservice.feature.contract.domain.enums.ContractType;
import com.inlaco.crewmgrservice.feature.contract.infrastructure.persistence.entity.ContractEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class LaborContractRepositoryAdapter implements LaborContractRepository {

  private final MongoTemplate mongoTemplate;

  @Override
  public boolean existsByApplicationId(String applicationId) {
    return mongoTemplate.exists(
        new Query()
            .addCriteria(
                Criteria.where("type")
                    .is(ContractType.LABOR_CONTRACT)
                    .and("searchMeta.applicationId")
                    .is(applicationId)),
        ContractEntity.class);
  }
}

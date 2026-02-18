package com.inlaco.crewmgrservice.feature.contract.infrastructure.persistence.adapter;

import com.inlaco.crewmgrservice.feature.contract.application.port.out.LaborContractRepository;
import com.inlaco.crewmgrservice.feature.contract.domain.enums.ContractType;
import com.inlaco.crewmgrservice.feature.contract.domain.model.Contract;
import com.inlaco.crewmgrservice.feature.contract.infrastructure.persistence.entity.contract.ContractEntity;
import com.inlaco.crewmgrservice.feature.contract.infrastructure.persistence.mapper.ContractEntityMapper;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class LaborContractRepositoryAdapter implements LaborContractRepository {

  private final MongoTemplate mongoTemplate;
  private final ContractEntityMapper mapper;

  @Override
  public boolean existsByApplicationId(String applicationId) {
    return mongoTemplate.exists(
        new Query()
            .addCriteria(
                Criteria.where("type")
                    .is(ContractType.LABOR_CONTRACT)
                    .and("applicationId")
                    .is(new ObjectId(applicationId))),
        ContractEntity.class);
  }

  @Override
  public Optional<Contract> findByApplicationId(String applicationId) {

    var entity =
        mongoTemplate.findOne(
            new Query()
                .addCriteria(
                    Criteria.where("type")
                        .is(ContractType.LABOR_CONTRACT)
                        .and("applicationId")
                        .is(new ObjectId(applicationId))),
            ContractEntity.class);
    if (entity == null) return Optional.empty();
    return Optional.of(mapper.toContract(entity));
  }
}

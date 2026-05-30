package com.inlaco.crewmgrservice.feature.contract.infrastructure.persistence.adapter;

import com.inlaco.crewmgrservice.feature.contract.application.port.out.CrewSupplyContractRepository;
import com.inlaco.crewmgrservice.feature.contract.domain.enums.ContractStatus;
import com.inlaco.crewmgrservice.feature.contract.domain.enums.ContractType;
import com.inlaco.crewmgrservice.feature.contract.domain.model.Contract;
import com.inlaco.crewmgrservice.feature.contract.infrastructure.persistence.entity.contract.ContractEntity;
import com.inlaco.crewmgrservice.feature.contract.infrastructure.persistence.mapper.ContractEntityMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CrewSupplyContractRepositoryAdapter implements CrewSupplyContractRepository {

  private final MongoTemplate mongoTemplate;
  private final ContractEntityMapper mapper;

  @Override
  public List<Contract> findActiveContractsByShipIMO(String shipImoNumber) {
    Query query =
        Query.query(
            new Criteria()
                .andOperator(
                    Criteria.where("status").is(ContractStatus.ACTIVE),
                    Criteria.where("type").is(ContractType.SUPPLY_CONTRACT),
                    Criteria.where("shipInfo.imoNumber").is(shipImoNumber)));

    List<ContractEntity> entities = mongoTemplate.find(query, ContractEntity.class);
    return entities.stream().map(mapper::toContract).toList();
  }
}

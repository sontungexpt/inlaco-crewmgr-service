package com.inlaco.crewmgrservice.feature.contract.repository;

import com.inlaco.crewmgrservice.feature.contract.model.AbstractContract;
import com.inlaco.crewmgrservice.feature.contract.model.Contract;
import com.inlaco.crewmgrservice.feature.contract.model.ContractType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
@RequiredArgsConstructor
public class CustomLaborContractRepository {

  private final MongoTemplate mongoTemplate;

  private Criteria buildFoundCriteriaContractByEmployeeId(String employeeId) {
    return Criteria.where("employeeId")
        .is(new ObjectId(employeeId))
        .and("type")
        .is(ContractType.LABOR_CONTRACT);
  }

  public Contract getLaborContractByEmployeeId(String employeeId) {
    Query query = new Query();
    query.addCriteria(buildFoundCriteriaContractByEmployeeId(employeeId));

    log.debug(query.toString());
    return mongoTemplate.findOne(query, AbstractContract.class);
  }

  public boolean existsLaborContractByEmployeeId(String employeeId) {
    Query query = new Query();
    query.addCriteria(buildFoundCriteriaContractByEmployeeId(employeeId));
    log.debug(query.toString());
    return mongoTemplate.exists(query, AbstractContract.class);
  }
}

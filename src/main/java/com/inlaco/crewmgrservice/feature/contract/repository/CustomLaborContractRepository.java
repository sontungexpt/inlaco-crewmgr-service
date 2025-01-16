package com.inlaco.crewmgrservice.feature.contract.repository;

import com.inlaco.crewmgrservice.feature.contract.model.AbstractContract;
import com.inlaco.crewmgrservice.feature.contract.model.Contract;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
@RequiredArgsConstructor
public class CustomLaborContractRepository {

  private final MongoTemplate mongoTemplate;

  public Contract getLaborContractByEmployeeId(String employeeId) {
    Query query = new Query();
    query.addCriteria(Criteria.where("employeeId").is(employeeId));
    log.debug(query.toString());
    return mongoTemplate.findOne(query, AbstractContract.class);
  }
}

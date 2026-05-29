package com.inlaco.crewmgrservice.feature.contract.infrastructure.persistence.adapter;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

import com.inlaco.crewmgrservice.feature.contract.application.model.ContractSearchCriteria;
import com.inlaco.crewmgrservice.feature.contract.application.port.out.ContractRepository;
import com.inlaco.crewmgrservice.feature.contract.domain.enums.ContractStatus;
import com.inlaco.crewmgrservice.feature.contract.domain.model.Contract;
import com.inlaco.crewmgrservice.feature.contract.infrastructure.persistence.entity.contract.ContractEntity;
import com.inlaco.crewmgrservice.feature.contract.infrastructure.persistence.mapper.ContractEntityMapper;
import com.inlaco.crewmgrservice.feature.contract.infrastructure.persistence.repository.ContractMongoRepository;
import com.inlaco.crewmgrservice.infrastructure.persistence.mongodb.aggregation.FacetResult;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ContractRepositoryAdapter implements ContractRepository {

  private final MongoTemplate mongoTemplate;
  private final ContractEntityMapper mapper;
  private final ContractMongoRepository repository;

  @Override
  public Page<Contract> findAll(Pageable pageable) {
    return repository.findAll(pageable).map(mapper::toContract);
  }

  @Override
  public Page<Contract> findAll(ContractSearchCriteria criteria, Pageable pageable) {

    List<Criteria> criteriaList = new ArrayList<>();

    if (criteria != null) {

      if (StringUtils.hasText(criteria.getKeyword())) {
        criteriaList.add(Criteria.where("title").regex(criteria.getKeyword(), "i"));
      }

      if (criteria.getType() != null) {
        criteriaList.add(Criteria.where("type").is(criteria.getType()));
      }

      if (criteria.getActivationDateStart() != null) {
        criteriaList.add(Criteria.where("activationDate").gte(criteria.getActivationDateStart()));
      }

      if (criteria.getActivationDateEnd() != null) {
        criteriaList.add(Criteria.where("activationDate").lte(criteria.getActivationDateEnd()));
      }

      if (criteria.getExpiredDateStart() != null) {
        criteriaList.add(Criteria.where("expiredDate").gte(criteria.getExpiredDateStart()));
      }

      if (criteria.getExpiredDateEnd() != null) {
        criteriaList.add(Criteria.where("expiredDate").lte(criteria.getExpiredDateEnd()));
      }

      if (criteria.getSigned() != null) {
        if (criteria.getSigned()) {
          criteriaList.add(Criteria.where("status").ne(ContractStatus.DRAFT));
        } else {
          criteriaList.add(Criteria.where("status").is(ContractStatus.DRAFT));
        }
      }

      if (criteria.getActive() != null) {
        if (criteria.getActive()) {
          criteriaList.add(Criteria.where("status").eq(ContractStatus.ACTIVE));
        } else {
          criteriaList.add(Criteria.where("status").ne(ContractStatus.ACTIVE));
        }
      }

      if (criteria.getIncludedStatuses() != null && !criteria.getIncludedStatuses().isEmpty()) {
        criteriaList.add(Criteria.where("status").in(criteria.getIncludedStatuses()));
      }

      if (criteria.getExcludedStatuses() != null && !criteria.getExcludedStatuses().isEmpty()) {
        criteriaList.add(Criteria.where("status").nin(criteria.getExcludedStatuses()));
      }

      if (criteria.getRelativeAccountId() != null) {
        criteriaList.add(
            new Criteria()
                .orOperator(
                    Criteria.where("initiator.accountId").is(criteria.getRelativeAccountId()),
                    Criteria.where("accountId").is(new ObjectId(criteria.getRelativeAccountId())),
                    Criteria.where("partners.accountId").is(criteria.getRelativeAccountId())));
      }
    }

    Criteria finalCriteria =
        criteriaList.isEmpty()
            ? new Criteria()
            : new Criteria().andOperator(criteriaList.toArray(new Criteria[0]));

    Aggregation aggregation =
        newAggregation(
            match(finalCriteria),
            facet(Aggregation.count().as(FacetResult.COUNT_KEY))
                .as(FacetResult.COUNT_FACET_NAME)
                .and(
                    sort(pageable.getSort()),
                    skip(pageable.getOffset()),
                    limit(pageable.getPageSize()))
                .as(FacetResult.DATA_FACET_NAME));

    return mongoTemplate
        .aggregate(aggregation, ContractEntity.class, ContractEntityFacetResult.class)
        .getUniqueMappedResult()
        .toPage(pageable)
        .map(mapper::toContract);
  }

  @Override
  public Optional<Contract> findById(String id) {
    return repository.findById(id).map(mapper::toContract);
  }

  @Override
  public Contract save(Contract contract) {
    String id = contract.getId();
    if (id == null) {
      return mapper.toContract(repository.insert(mapper.toContractEntity(contract)));
    }

    ContractEntity entity =
        repository
            .findById(id)
            .map(
                existing -> {
                  mapper.updateFromContract(contract, existing);
                  return existing;
                })
            .orElseGet(() -> mapper.toContractEntity(contract)); // INSERT with custom id
    return mapper.toContract(repository.save(entity));
  }

  @Override
  public List<Contract> saveAll(Iterable<Contract> contracts) {
    List<ContractEntity> entities = new ArrayList<>();
    for (Contract contract : contracts) {
      entities.add(mapper.toContractEntity(contract));
    }
    return repository.saveAll(entities).stream().map(mapper::toContract).toList();
  }

  @Override
  public List<Contract> findDueForActivation(Instant now) {
    Query query =
        Query.query(
            Criteria.where("status").is(ContractStatus.SIGNED).and("activationDate").lte(now));
    List<ContractEntity> entities = mongoTemplate.find(query, ContractEntity.class);
    return entities.stream().map(mapper::toContract).toList();
  }

  @Override
  public List<Contract> findDueForExpiration(Instant now) {
    Query query =
        Query.query(Criteria.where("status").is(ContractStatus.ACTIVE).and("expiredDate").lte(now));
    List<ContractEntity> entities = mongoTemplate.find(query, ContractEntity.class);
    return entities.stream().map(mapper::toContract).toList();
  }

  private class ContractEntityFacetResult extends FacetResult<ContractEntity> {}
}

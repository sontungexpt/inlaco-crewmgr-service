package com.inlaco.crewmgrservice.feature.contract.infrastructure.persistence.adapter;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

import com.inlaco.crewmgrservice.feature.contract.application.model.ContractSearchCriteria;
import com.inlaco.crewmgrservice.feature.contract.application.port.out.ContractRepository;
import com.inlaco.crewmgrservice.feature.contract.domain.enums.ContractStatus;
import com.inlaco.crewmgrservice.feature.contract.domain.model.AbstractContract;
import com.inlaco.crewmgrservice.feature.contract.infrastructure.persistence.entity.ContractEntity;
import com.inlaco.crewmgrservice.feature.contract.infrastructure.persistence.mapper.ContractEntityMapper;
import com.inlaco.crewmgrservice.feature.contract.infrastructure.persistence.repository.ContractMongoRepository;
import com.inlaco.crewmgrservice.infrastructure.persistence.mongodb.aggregation.FacetResult;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Slf4j
public class ContractRepositoryAdapter implements ContractRepository {
  private final MongoTemplate mongoTemplate;
  private final ContractEntityMapper mapper;
  private final ContractMongoRepository repository;

  @Override
  public Page<AbstractContract> findAll(Pageable pageable) {
    return repository.findAll(pageable).map(mapper::toContract);
  }

  @Override
  public Page<AbstractContract> findAll(ContractSearchCriteria criteria, Pageable pageable) {
    Criteria query = new Criteria();
    if (criteria != null) {
      if (criteria.getType() != null) {
        query.and("type").is(criteria.getType());
      }
      if (criteria.getActivationDateStart() != null) {
        query.and("activationDate").gte(criteria.getActivationDateStart());
      }
      if (criteria.getActivationDateEnd() != null) {
        query.and("activationDate").lte(criteria.getActivationDateEnd());
      }
      if (criteria.getExpiredDateStart() != null) {
        query.and("expiredDate").gte(criteria.getExpiredDateStart());
      }
      if (criteria.getExpiredDateEnd() != null) {
        query.and("expiredDate").lte(criteria.getExpiredDateEnd());
      }
      if (criteria.getSigned() != null) {
        query.and("signed").is(criteria.getSigned());
      }
    }

    Aggregation aggregation =
        newAggregation(
            match(query),
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
  public Optional<AbstractContract> findById(String id) {
    return repository.findById(id).map(mapper::toContract);
  }

  @Override
  public AbstractContract save(AbstractContract contract) {
    return mapper.toContract(repository.save(mapper.toContractEntity(contract)));
  }

  @Override
  public List<AbstractContract> saveAll(Iterable<AbstractContract> contracts) {
    List<ContractEntity> entities = new ArrayList<>();
    for (AbstractContract contract : contracts) {
      entities.add(mapper.toContractEntity(contract));
    }
    return repository.saveAll(entities).stream().map(mapper::toContract).toList();
  }

  @Override
  public List<AbstractContract> findDueForActivation(Instant now) {
    Query query =
        Query.query(
            Criteria.where("status").is(ContractStatus.SIGNED).and("activationDate").lte(now));
    List<ContractEntity> entities = mongoTemplate.find(query, ContractEntity.class);
    return entities.stream().map(mapper::toContract).toList();
  }

  @Override
  public List<AbstractContract> findDueForExpiration(Instant now) {
    Query query =
        Query.query(Criteria.where("status").is(ContractStatus.ACTIVE).and("expiredDate").lte(now));
    List<ContractEntity> entities = mongoTemplate.find(query, ContractEntity.class);
    return entities.stream().map(mapper::toContract).toList();
  }

  private class ContractEntityFacetResult extends FacetResult<ContractEntity> {}
}

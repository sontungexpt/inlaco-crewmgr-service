package com.inlaco.crewmgrservice.feature.contract.repository;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

import com.inlaco.crewmgrservice.common.model.FacetResult;
import com.inlaco.crewmgrservice.feature.contract.model.AbstractContract;
import com.inlaco.crewmgrservice.feature.contract.presentation.dto.ContractFilterable;
import com.inlaco.crewmgrservice.utils.PageableUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Slf4j
public class CustomContractRepository {

  private final MongoTemplate mongoTemplate;

  public Page<AbstractContract> findAllContracts(ContractFilterable filterable, Pageable p) {
    List<AggregationOperation> operations = new ArrayList<>();

    if (filterable != null && filterable.isFilterable()) {
      operations.add(match(buildFilterableCriteria(filterable)));
    }

    var pageable = PageableUtils.extendDefaultSort(p);

    operations.add(buildSimplePaginationOperation(pageable));

    var result =
        mongoTemplate
            .aggregate(
                newAggregation(operations), AbstractContract.class, ContractFacetResult.class)
            .getUniqueMappedResult();

    return result.toPage(pageable);
  }

  private AggregationOperation buildSimplePaginationOperation(Pageable pageable) {
    return Aggregation.facet(Aggregation.count().as(FacetResult.getCountKey()))
        .as(FacetResult.getCountFacetName())
        .and(sort(pageable.getSort()), skip(pageable.getOffset()), limit(pageable.getPageSize()))
        .as(FacetResult.getDataFacetName());
  }

  private Criteria buildFilterableCriteria(ContractFilterable filterable) {
    Criteria criteria = new Criteria();
    if (filterable.getType() != null) {
      log.info("Filtering by type: {}", filterable.getType());
      criteria.and("type").is(filterable.getType());
    }
    if (filterable.getActivationDateStart() != null) {
      log.info("Filtering by activationDateStart: {}", filterable.getActivationDateStart());
      criteria.and("activationDate").gte(filterable.getActivationDateStart());
    }
    if (filterable.getActivationDateEnd() != null) {
      log.info("Filtering by activationDateEnd: {}", filterable.getActivationDateEnd());
      criteria.and("activationDate").lte(filterable.getActivationDateEnd());
    }
    if (filterable.getExpiredDateStart() != null) {
      log.info("Filtering by expiredDateStart: {}", filterable.getExpiredDateStart());
      criteria.and("expiredDate").gte(filterable.getExpiredDateStart());
    }
    if (filterable.getExpiredDateEnd() != null) {
      log.info("Filtering by expiredDateEnd: {}", filterable.getExpiredDateEnd());
      criteria.and("expiredDate").lte(filterable.getExpiredDateEnd());
    }
    if (filterable.getSigned() != null) {
      log.info("Filtering by signed: {}", filterable.getSigned());
      criteria.and("signed").is(filterable.getSigned());
    }
    return criteria;
  }

  private class ContractFacetResult extends FacetResult<AbstractContract> {
    public ContractFacetResult(
        List<AbstractContract> dataFacet, List<Map<String, Object>> countFacet) {
      super(dataFacet, countFacet);
    }
  }
}

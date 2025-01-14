package com.inlaco.crewmgrservice.feature.contract.repository;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

import com.inlaco.crewmgrservice.common.model.FacetResult;
import com.inlaco.crewmgrservice.feature.contract.dto.ContractFilterable;
import com.inlaco.crewmgrservice.feature.contract.model.AbstractContract;
import com.inlaco.crewmgrservice.utils.PageableUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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

  private Criteria buildFilterCriteria(ContractFilterable filter) {
    Criteria criteria = new Criteria();
    if (filter.getType() != null) {
      criteria.and("type").is(filter.getType());
    }
    if (filter.getActivationDateStart() != null) {
      criteria.and("activationDate").gte(filter.getActivationDateStart());
    }
    if (filter.getActivationDateEnd() != null) {
      criteria.and("activationDate").lte(filter.getActivationDateEnd());
    }
    if (filter.getExpiredDateStart() != null) {
      criteria.and("expiredDate").gte(filter.getExpiredDateStart());
    }
    if (filter.getExpiredDateEnd() != null) {
      criteria.and("expiredDate").lte(filter.getExpiredDateEnd());
    }
    if (filter.getSigned() != null) {
      criteria.and("signed").is(filter.getSigned());
    }
    return criteria;
  }

  public Page<AbstractContract> findAllContracts(ContractFilterable filter, Pageable p) {
    List<AggregationOperation> operations = new ArrayList<>();
    if (filter.isFilterable()) {
      operations.add(match(buildFilterCriteria(filter)));
    }

    var pageable = PageableUtils.extendDefaultSort(p);
    operations.add(
        Aggregation.facet(Aggregation.count().as(FacetResult.getCountKey()))
            .as(FacetResult.getCountFacetName())
            .and(
                sort(pageable.getSort()), skip(pageable.getOffset()), limit(pageable.getPageSize()))
            .as(FacetResult.getDataFacetName()));

    Aggregation aggregation = newAggregation(operations);

    var result =
        mongoTemplate
            .aggregate(aggregation, AbstractContract.class, ContractFacetResult.class)
            .getUniqueMappedResult();

    return new PageImpl<>(result.getDatas(), pageable, result.getCount("totalSailors"));
  }

  private static class ContractFacetResult extends FacetResult<AbstractContract> {

    public ContractFacetResult(
        List<AbstractContract> dataFacet, List<Map<String, Object>> countFacet) {
      super(dataFacet, countFacet);
    }
  }
}

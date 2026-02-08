package com.inlaco.crewmgrservice.feature.crewrental.repository;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.limit;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.skip;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.sort;

import com.inlaco.crewmgrservice.common.model.FacetResult;
import com.inlaco.crewmgrservice.feature.crewrental.dto.RentalRequestFilterable;
import com.inlaco.crewmgrservice.feature.crewrental.model.RentalRequest;
import com.inlaco.crewmgrservice.shared.constant.PhoneNumberRegexp;
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

@Slf4j
@Repository
@RequiredArgsConstructor
public class CustomRentalRequestRepository {

  private final MongoTemplate mongoTemplate;

  private AggregationOperation buildPaginationOperation(Pageable pageable) {
    return Aggregation.facet(Aggregation.count().as(FacetResult.getCountKey()))
        .as(FacetResult.getCountFacetName())
        .and(sort(pageable.getSort()), skip(pageable.getOffset()), limit(pageable.getPageSize()))
        .as(FacetResult.getDataFacetName());
  }

  public Page<RentalRequest> searchRequests(
      String keyword, RentalRequestFilterable filter, Pageable p) {
    List<Criteria> criteriaList = new ArrayList<>();
    if (PhoneNumberRegexp.ITU_T_E_164.isValid(keyword)) {
      criteriaList.add(Criteria.where("companyPhone").regex(keyword, "i"));
    }
    criteriaList.add(Criteria.where("companyEmail").regex(keyword, "i"));
    criteriaList.add(Criteria.where("companyName").regex(keyword, "i"));
    criteriaList.add(Criteria.where("shipInfo.name").regex(keyword, "i"));

    var query = new Criteria().orOperator(criteriaList);
    if (filter.isFilterable()) {
      query.andOperator(buildFilterCriteria(filter));
    }

    log.debug("Fetching rental requests with keyword: {}", keyword);

    var pageable = PageableUtils.extendDefaultSort(p);
    Aggregation aggregation =
        Aggregation.newAggregation(match(query), buildPaginationOperation(pageable));
    return executeAggregation(aggregation, pageable, RentalRequest.class);
  }

  public Criteria buildFilterCriteria(RentalRequestFilterable filter) {
    Criteria criteria = new Criteria();
    String keyword = filter.getKeyword();
    if (keyword != null) {
      if (PhoneNumberRegexp.ITU_T_E_164.isValid(keyword)) {
        criteria.andOperator(Criteria.where("companyPhone").regex(keyword, "i"));
      }
      criteria.andOperator(
          Criteria.where("companyEmail").regex(keyword, "i"),
          Criteria.where("companyName").regex(keyword, "i"),
          Criteria.where("shipInfo.name").regex(keyword, "i"));
    }

    if (filter.getStatus() != null) {
      criteria.andOperator(Criteria.where("status").is(filter.getStatus()));
    }

    return criteria;
  }

  public Page<RentalRequest> findAllRequests(RentalRequestFilterable filterable, Pageable p) {
    var pageable = PageableUtils.extendDefaultSort(p);
    log.debug("Fetching rental requests with filter: {}", filterable);

    List<AggregationOperation> operations = new ArrayList<>();

    if (filterable != null && filterable.isFilterable()) {
      operations.add(match(buildFilterCriteria(filterable)));
    }
    operations.add(buildPaginationOperation(pageable));

    Aggregation aggregation = Aggregation.newAggregation(operations);
    return executeAggregation(aggregation, pageable, RentalRequest.class);
  }

  private Page<RentalRequest> executeAggregation(
      Aggregation aggregation, Pageable pageable, Class<RentalRequest> clazz) {
    var result =
        mongoTemplate
            .aggregate(aggregation, clazz, CrewRentalRequestFacetResult.class)
            .getUniqueMappedResult();
    return result.toPage(pageable);
  }

  private class CrewRentalRequestFacetResult extends FacetResult<RentalRequest> {
    public CrewRentalRequestFacetResult(
        List<RentalRequest> dataFacet, List<Map<String, Object>> countFacet) {
      super(dataFacet, countFacet);
    }
  }
}

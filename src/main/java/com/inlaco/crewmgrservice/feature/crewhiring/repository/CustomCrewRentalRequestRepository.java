package com.inlaco.crewmgrservice.feature.crewhiring.repository;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.limit;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.skip;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.sort;

import com.inlaco.crewmgrservice.common.model.FacetResult;
import com.inlaco.crewmgrservice.feature.crewhiring.dto.CrewRentalRequestFilter;
import com.inlaco.crewmgrservice.feature.crewhiring.model.CrewRentalRequest;
import com.inlaco.crewmgrservice.utils.PageableUtils;
import com.inlaco.crewmgrservice.utils.PhoneNumberValidatorUtils;
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

@Slf4j
@Repository
@RequiredArgsConstructor
public class CustomCrewRentalRequestRepository {

  private final MongoTemplate mongoTemplate;

  private AggregationOperation buildPaginationOperation(Pageable pageable) {
    return Aggregation.facet(Aggregation.count().as(FacetResult.getCountKey()))
        .as(FacetResult.getCountFacetName())
        .and(sort(pageable.getSort()), skip(pageable.getOffset()), limit(pageable.getPageSize()))
        .as(FacetResult.getDataFacetName());
  }

  public Page<CrewRentalRequest> searchRequests(
      String keyword, CrewRentalRequestFilter filter, Pageable p) {
    List<Criteria> criteriaList = new ArrayList<>();
    if (PhoneNumberValidatorUtils.isPotentialPhoneNumber(keyword)) {
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
    return executeAggregation(aggregation, pageable, CrewRentalRequest.class);
  }

  public Criteria buildFilterCriteria(CrewRentalRequestFilter filter) {
    List<Criteria> criteriaList = new ArrayList<>();
    if (filter.getStatus() != null) {
      criteriaList.add(Criteria.where("status").is(filter.getStatus()));
    }
    return new Criteria().andOperator(criteriaList);
  }

  public Page<CrewRentalRequest> findAllRequests(CrewRentalRequestFilter filter, Pageable p) {

    var pageable = PageableUtils.extendDefaultSort(p);
    log.debug("Fetching rental requests with filter: {}", filter);

    List<AggregationOperation> operations = new ArrayList<>();
    if (filter.isFilterable()) {
      operations.add(match(buildFilterCriteria(filter)));
    }
    operations.add(buildPaginationOperation(pageable));

    Aggregation aggregation =
        Aggregation.newAggregation(operations.toArray(new AggregationOperation[0]));
    return executeAggregation(aggregation, pageable, CrewRentalRequest.class);
  }

  private Page<CrewRentalRequest> executeAggregation(
      Aggregation aggregation, Pageable pageable, Class<CrewRentalRequest> clazz) {
    var result =
        mongoTemplate
            .aggregate(aggregation, clazz, CrewRentalRequestFacetResult.class)
            .getUniqueMappedResult();
    return new PageImpl<>(result.getDatas(), pageable, result.getCount());
  }

  private static class CrewRentalRequestFacetResult extends FacetResult<CrewRentalRequest> {

    public CrewRentalRequestFacetResult(
        List<CrewRentalRequest> dataFacet, List<Map<String, Object>> countFacet) {
      super(dataFacet, countFacet);
    }
  }
}

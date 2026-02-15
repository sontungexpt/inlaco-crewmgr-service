package com.inlaco.crewmgrservice.infrastructure.persistence.support;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.limit;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.skip;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.sort;

import com.inlaco.crewmgrservice.common.model.FacetResult;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.query.Criteria;

public final class AggregationUtils {

  private AggregationUtils() {}

  public static AggregationOperation paginationFacet(Pageable pageable) {
    return Aggregation.facet(Aggregation.count().as(FacetResult.getCountKey()))
        .as(FacetResult.getCountFacetName())
        .and(sort(pageable.getSort()), skip(pageable.getOffset()), limit(pageable.getPageSize()))
        .as(FacetResult.getDataFacetName());
  }

  public static List<AggregationOperation> matchIfNotEmpty(Criteria criteria) {
    return criteria == null || criteria.getCriteriaObject().isEmpty()
        ? List.of()
        : List.of(match(criteria));
  }
}

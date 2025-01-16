package com.inlaco.crewmgrservice.feature.schedule.repository;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;
import static org.springframework.data.mongodb.core.query.Criteria.*;
import static org.springframework.data.mongodb.core.query.Query.*;

import com.inlaco.crewmgrservice.common.model.FacetResult;
import com.inlaco.crewmgrservice.feature.schedule.dto.ScheduleFilterable;
import com.inlaco.crewmgrservice.feature.schedule.model.Schedule;
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
public class CustomScheduleRepository {

  private final MongoTemplate mongoTemplate;

  public Page<Schedule> findAllSchedules(ScheduleFilterable filterable, Pageable pageable) {
    List<AggregationOperation> operations = new ArrayList<>();

    if (filterable.getStatus() != null) {
      var criteria = buildFilterCriteria(filterable);
      operations.add(match(criteria));
    }

    operations.add(buildSimplePaginationOperation(pageable));

    Aggregation aggregation = Aggregation.newAggregation(operations);

    var result =
        mongoTemplate
            .aggregate(aggregation, Schedule.class, ScheduleFacetResult.class)
            .getUniqueMappedResult();

    return result.toPage(pageable);
  }

  public Page<Schedule> findScheduleByCardId(
      String cardId, ScheduleFilterable filterable, Pageable pageable) {
    List<AggregationOperation> operations = new ArrayList<>();

    var criteria = Criteria.where("crewMembers.cardId").is(cardId);

    if (filterable.getStatus() != null) {
      criteria.andOperator(buildFilterCriteria(filterable));
    }

    operations.add(match(criteria));
    operations.add(buildSimplePaginationOperation(pageable));

    Aggregation aggregation = Aggregation.newAggregation(operations);

    var result =
        mongoTemplate
            .aggregate(aggregation, Schedule.class, ScheduleFacetResult.class)
            .getUniqueMappedResult();

    return result.toPage(pageable);
  }

  private Criteria buildFilterCriteria(ScheduleFilterable filterable) {
    Criteria criteria = new Criteria();

    if (filterable.getStatus() != null) {
      criteria.and("status").is(filterable.getStatus());
    }

    return criteria;
  }

  private AggregationOperation buildSimplePaginationOperation(Pageable pageable) {
    return Aggregation.facet(Aggregation.count().as(FacetResult.getCountKey()))
        .as(FacetResult.getCountFacetName())
        .and(sort(pageable.getSort()), skip(pageable.getOffset()), limit(pageable.getPageSize()))
        .as(FacetResult.getDataFacetName());
  }

  private class ScheduleFacetResult extends FacetResult<Schedule> {
    public ScheduleFacetResult(List<Schedule> dataFacet, List<Map<String, Object>> countFacet) {
      super(dataFacet, countFacet);
    }
  }
}

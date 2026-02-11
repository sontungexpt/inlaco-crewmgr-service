package com.inlaco.crewmgrservice.feature.schedule.repository;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

import com.inlaco.crewmgrservice.common.model.FacetResult;
import com.inlaco.crewmgrservice.feature.crew.infrastructure.persistence.mongodb.entity.CrewProfileEntity;
import com.inlaco.crewmgrservice.feature.schedule.dto.MobilizationResponse;
import com.inlaco.crewmgrservice.feature.schedule.dto.ScheduleFilterable;
import com.inlaco.crewmgrservice.feature.schedule.model.AssignedMobilization;
import com.inlaco.crewmgrservice.utils.PageableUtils;
import java.time.*;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Slf4j
public class CustomScheduleRepository {

  private final MongoTemplate mongoTemplate;

  public MobilizationResponse findDetailSchedule(String id) {

    // AssigmentSchedule schedule = mongoTemplate.findById(id, AssigmentSchedule.class);
    // ScheduleResponse response = ScheduleResponse.from(schedule);
    // List<SailorProfile> profiles =
    //     mongoTemplate.find(
    //         new Query()
    //             .addCriteria(
    //                 Criteria.where("cardId")
    //                     .in(schedule.getCrewMembers().stream().map(it ->
    // it.getCardId()).toList())),
    //         SailorProfile.class);

    // response.setCrewMembers(profiles);

    Aggregation aggregation =
        newAggregation(
            match(Criteria.where("_id").is(id)),
            lookup(
                mongoTemplate.getCollectionName(CrewProfileEntity.class),
                "cardId",
                "crewMembers.employeeCardId",
                "crewMembers"));

    return mongoTemplate
        .aggregate(aggregation, AssignedMobilization.class, MobilizationResponse.class)
        .getUniqueMappedResult();
  }

  private ScheduleFilterable defaultWeekStartEndFilter(ScheduleFilterable filterable) {
    if (filterable == null) return null;
    else if (filterable.getStartDate() == null && filterable.getEndDate() == null) {
      Instant now = Instant.now();

      ZoneId zoneId = ZoneId.systemDefault();
      ZonedDateTime zonedNow = now.atZone(zoneId);

      ZonedDateTime startOfWeek =
          zonedNow.with(DayOfWeek.MONDAY).toLocalDate().atStartOfDay(zoneId);

      ZonedDateTime endOfWeek =
          zonedNow.with(DayOfWeek.SUNDAY).toLocalDate().atTime(LocalTime.MAX).atZone(zoneId);

      filterable.setStartDate(startOfWeek.toInstant());
      filterable.setEndDate(endOfWeek.toInstant());
    }
    return filterable;
  }

  public List<AssignedMobilization> findAllSchedules(ScheduleFilterable filterable) {
    return mongoTemplate.find(
        new Query().addCriteria(buildFilterableCriteria(defaultWeekStartEndFilter(filterable))),
        AssignedMobilization.class);
  }

  public Page<AssignedMobilization> findAllSchedules(
      ScheduleFilterable filterable, Pageable pageable) {
    List<AggregationOperation> operations = new ArrayList<>();

    if (filterable != null && filterable.getStatus() != null) {
      operations.add(match(buildFilterableCriteria(filterable)));
    }

    pageable = PageableUtils.extendDefaultSort(pageable, AssignedMobilization.class);
    operations.add(buildSimplePaginationOperation(pageable));

    var result =
        mongoTemplate
            .aggregate(
                newAggregation(operations), AssignedMobilization.class, ScheduleFacetResult.class)
            .getUniqueMappedResult();

    return result.toPage(pageable);
  }

  public List<AssignedMobilization> findSchedulesByCardId(
      String cardId, ScheduleFilterable filterable) {
    List<AggregationOperation> operations = new ArrayList<>();
    var criteria = Criteria.where("crewMembers.cardId").is(cardId);

    if (filterable != null && filterable.getStatus() != null) {
      criteria.andOperator(buildFilterableCriteria(filterable));
    }

    operations.add(match(criteria));

    return mongoTemplate.find(new Query().addCriteria(criteria), AssignedMobilization.class);
  }

  public Page<AssignedMobilization> findPaginationSchedulesByCardId(
      String cardId, ScheduleFilterable filterable, Pageable pageable) {
    List<AggregationOperation> operations = new ArrayList<>();

    var criteria = Criteria.where("crewMembers.cardId").is(cardId);

    if (filterable != null && filterable.getStatus() != null) {
      criteria.andOperator(buildFilterableCriteria(filterable));
    }

    operations.add(match(criteria));

    pageable = PageableUtils.extendDefaultSort(pageable, AssignedMobilization.class);
    operations.add(buildSimplePaginationOperation(pageable));

    var result =
        mongoTemplate
            .aggregate(
                newAggregation(operations), AssignedMobilization.class, ScheduleFacetResult.class)
            .getUniqueMappedResult();

    return result.toPage(pageable);
  }

  private Criteria buildFilterableCriteria(ScheduleFilterable filterable) {

    List<Criteria> criteriaList = new ArrayList<>();

    Criteria criteria = new Criteria();

    if (filterable.getStatus() != null) {
      criteria.and("status").is(filterable.getStatus());
    }

    if (filterable.getStartDate() != null) {
      Criteria criteria1 = Criteria.where("startDate").gte(filterable.getStartDate());
      if (filterable.getEndDate() != null) {
        criteria1.and("estimatedEndDate").lte(filterable.getEndDate());
      }
      criteriaList.add(criteria1);
    }

    if (filterable.getEndDate() != null) {
      Criteria criteria2 = Criteria.where("estimatedEndDate").lte(filterable.getEndDate());
      if (filterable.getStartDate() != null) {
        criteria2.and("startDate").gte(filterable.getStartDate());
      }
      criteriaList.add(criteria2);
    }

    criteriaList.add(criteria);
    return criteria.andOperator(criteriaList);
  }

  private AggregationOperation buildSimplePaginationOperation(Pageable pageable) {
    return Aggregation.facet(Aggregation.count().as(FacetResult.getCountKey()))
        .as(FacetResult.getCountFacetName())
        .and(sort(pageable.getSort()), skip(pageable.getOffset()), limit(pageable.getPageSize()))
        .as(FacetResult.getDataFacetName());
  }

  static class ScheduleFacetResult extends FacetResult<AssignedMobilization> {}
}

package com.inlaco.crewmgrservice.feature.schedule.infrastructure.persistence.mongodb.adapter;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

import com.inlaco.crewmgrservice.feature.schedule.application.model.CrewMobilizationScheduleSearchCriteria;
import com.inlaco.crewmgrservice.feature.schedule.application.port.out.CrewMobilizationScheduleRepository;
import com.inlaco.crewmgrservice.feature.schedule.domain.model.CrewMobilizationSchedule;
import com.inlaco.crewmgrservice.feature.schedule.infrastructure.persistence.mongodb.entity.CrewMobilizationScheduleEntity;
import com.inlaco.crewmgrservice.feature.schedule.infrastructure.persistence.mongodb.mapper.CrewMobilizationScheduleEntityMapper;
import com.inlaco.crewmgrservice.feature.schedule.infrastructure.persistence.mongodb.repository.CrewMobilizationScheduleMongoRepository;
import com.inlaco.crewmgrservice.infrastructure.persistence.mongodb.aggregation.FacetResult;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
@Slf4j
public class CrewMobilizationScheduleRepositoryAdpater
    implements CrewMobilizationScheduleRepository {

  private final CrewMobilizationScheduleMongoRepository repository;
  private final CrewMobilizationScheduleEntityMapper mapper;
  private final MongoTemplate mongoTemplate;

  @Override
  public CrewMobilizationSchedule save(CrewMobilizationSchedule schedule) {
    return mapper.toCrewMobilizationSchedule(
        repository.save(mapper.toCrewMobilizationScheduleEntity(schedule)));
  }

  @Override
  public Optional<CrewMobilizationSchedule> findById(String id) {
    return repository.findById(id).map(mapper::toCrewMobilizationSchedule);
  }

  @Override
  public Page<CrewMobilizationSchedule> findAll(
      CrewMobilizationScheduleSearchCriteria criteria, Pageable pageable) {
    var query = new Criteria();

    if (criteria != null) {
      if (criteria.getStatus() != null) {
        query.and("status").is(criteria.getStatus());
      }

      if (criteria.getStartDate() != null) {
        query.and("startDate").gte(criteria.getStartDate());
      }

      if (criteria.getEndDate() != null) {
        query.and("endDate").lte(criteria.getEndDate());
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
        .aggregate(
            aggregation,
            CrewMobilizationScheduleEntity.class,
            CrewMobilizationScheduleFacetResult.class)
        .getUniqueMappedResult()
        .toPage(pageable)
        .map(mapper::toCrewMobilizationSchedule);
  }

  @Override
  public Page<CrewMobilizationSchedule> findAll(Pageable pageable) {
    return repository.findAll(pageable).map(mapper::toCrewMobilizationSchedule);
  }

  static class CrewMobilizationScheduleFacetResult
      extends FacetResult<CrewMobilizationScheduleEntity> {}
}

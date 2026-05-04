package com.inlaco.crewmgrservice.feature.crewmobilization.infrastructure.persistence.mongodb.adapter;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

import com.inlaco.crewmgrservice.feature.crewmobilization.application.model.CrewMobilizationSearchCriteria;
import com.inlaco.crewmgrservice.feature.crewmobilization.application.port.out.CrewMobilizationRepository;
import com.inlaco.crewmgrservice.feature.crewmobilization.domain.model.CrewMobilizationSchedule;
import com.inlaco.crewmgrservice.feature.crewmobilization.infrastructure.persistence.mongodb.entity.CrewMobilizationEntity;
import com.inlaco.crewmgrservice.feature.crewmobilization.infrastructure.persistence.mongodb.mapper.CrewMobilizationEntityMapper;
import com.inlaco.crewmgrservice.feature.crewmobilization.infrastructure.persistence.mongodb.repository.CrewMobilizationMongoRepository;
import com.inlaco.crewmgrservice.infrastructure.persistence.mongodb.aggregation.FacetResult;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
@Slf4j
public class CrewMobilizationRepositoryAdpater implements CrewMobilizationRepository {

  private final CrewMobilizationMongoRepository repository;
  private final CrewMobilizationEntityMapper mapper;
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
      CrewMobilizationSearchCriteria criteria, Pageable pageable) {
    var query = new Criteria();

    if (criteria != null) {
      if (criteria.getKeyword() != null && !criteria.getKeyword().isEmpty()) {
        query.orOperator(
            Criteria.where("partnerName").regex(criteria.getKeyword(), "i"),
            Criteria.where("shipInfo.name").regex(criteria.getKeyword(), "i"));
      }
      if (criteria.getStatus() != null) {
        query.and("status").is(criteria.getStatus());
      }

      if (criteria.getStartDate() != null) {
        query.and("startDate").gte(criteria.getStartDate());
      }

      if (criteria.getEndDate() != null) {
        query.and("endDate").lte(criteria.getEndDate());
      }

      if (criteria.getAccountId() != null && !criteria.getAccountId().isEmpty()) {
        query.and("crews.accountId").is(new ObjectId(criteria.getAccountId()));
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
            aggregation, CrewMobilizationEntity.class, CrewMobilizationScheduleFacetResult.class)
        .getUniqueMappedResult()
        .toPage(pageable)
        .map(mapper::toCrewMobilizationSchedule);
  }

  @Override
  public Page<CrewMobilizationSchedule> findAll(Pageable pageable) {
    return repository.findAll(pageable).map(mapper::toCrewMobilizationSchedule);
  }

  static class CrewMobilizationScheduleFacetResult extends FacetResult<CrewMobilizationEntity> {}
}

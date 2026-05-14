package com.inlaco.crewmgrservice.feature.crewmobilization.infrastructure.persistence.mongodb.adapter;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

import com.inlaco.crewmgrservice.feature.crewmobilization.application.model.CrewMobilizationSearchCriteria;
import com.inlaco.crewmgrservice.feature.crewmobilization.application.port.out.CrewMobilizationRepository;
import com.inlaco.crewmgrservice.feature.crewmobilization.domain.model.CrewMobilization;
import com.inlaco.crewmgrservice.feature.crewmobilization.infrastructure.persistence.mongodb.entity.CrewMobilizationEntity;
import com.inlaco.crewmgrservice.feature.crewmobilization.infrastructure.persistence.mongodb.mapper.CrewMobilizationEntityMapper;
import com.inlaco.crewmgrservice.feature.crewmobilization.infrastructure.persistence.mongodb.repository.CrewMobilizationMongoRepository;
import com.inlaco.crewmgrservice.infrastructure.persistence.mongodb.aggregation.FacetResult;
import java.util.ArrayList;
import java.util.List;
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
  public CrewMobilization save(CrewMobilization mobilization) {
    String id = mobilization.getId();
    if (id == null) {
      return mapper.toCrewMobilization(
          repository.insert(mapper.toCrewMobilizationEntity(mobilization)));
    }
    CrewMobilizationEntity entity =
        repository
            .findById(id)
            .map(
                existing -> {
                  mapper.updateFromCrewMobilization(mobilization, existing);
                  return existing;
                })
            .orElseGet(() -> mapper.toCrewMobilizationEntity(mobilization));
    return mapper.toCrewMobilization(repository.save(entity));
  }

  @Override
  public Optional<CrewMobilization> findById(String id) {
    return repository.findById(id).map(mapper::toCrewMobilization);
  }

  @Override
  public Page<CrewMobilization> findAll(
      CrewMobilizationSearchCriteria criteria, Pageable pageable) {

    Criteria matchCriteria = buildCriteria(criteria);

    Aggregation aggregation =
        newAggregation(
            lookup("crew_mobilization_assignments", "_id", "mobilizationId", "crews"),
            match(matchCriteria),
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
        .map(mapper::toCrewMobilization);
  }

  private Criteria buildCriteria(CrewMobilizationSearchCriteria criteria) {
    List<Criteria> andCriteria = new ArrayList<>();
    if (criteria == null) {
      return new Criteria();
    }

    if (criteria.getKeyword() != null && !criteria.getKeyword().isBlank()) {

      String keyword = criteria.getKeyword().trim();

      andCriteria.add(
          new Criteria()
              .orOperator(
                  Criteria.where("partnerName").regex(keyword, "i"),
                  Criteria.where("shipInfo.name").regex(keyword, "i"),
                  Criteria.where("shipInfo.imoNumber").regex(keyword, "i")));
    }

    if (criteria.getStatus() != null) {

      andCriteria.add(Criteria.where("status").is(criteria.getStatus()));
    }

    if (criteria.getStartDate() != null) {

      andCriteria.add(Criteria.where("startDate").gte(criteria.getStartDate()));
    }

    if (criteria.getEndDate() != null) {

      andCriteria.add(Criteria.where("endDate").lte(criteria.getEndDate()));
    }

    /*
     * ACCOUNT ID
     */
    if (criteria.getAccountId() != null && !criteria.getAccountId().isBlank()) {

      andCriteria.add(Criteria.where("crews.accountId").is(new ObjectId(criteria.getAccountId())));
    }
    if (criteria.getClientId() != null && !criteria.getClientId().isBlank()) {
      andCriteria.add(Criteria.where("partnerAccountId").is(new ObjectId(criteria.getClientId())));
    }

    if (criteria.getShipIMO() != null && !criteria.getShipIMO().isBlank()) {
      andCriteria.add(Criteria.where("shipInfo.imoNumber").is(criteria.getShipIMO()));
    }

    if (andCriteria.isEmpty()) {
      return new Criteria();
    }

    return new Criteria().andOperator(andCriteria);
  }

  @Override
  public Page<CrewMobilization> findAll(Pageable pageable) {
    return repository.findAll(pageable).map(mapper::toCrewMobilization);
  }

  static class CrewMobilizationScheduleFacetResult extends FacetResult<CrewMobilizationEntity> {}
}

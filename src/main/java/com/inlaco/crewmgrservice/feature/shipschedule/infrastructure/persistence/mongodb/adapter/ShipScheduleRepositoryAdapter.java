package com.inlaco.crewmgrservice.feature.shipschedule.infrastructure.persistence.mongodb.adapter;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

import com.inlaco.crewmgrservice.feature.shipschedule.application.model.ShipScheduleSearchCriteria;
import com.inlaco.crewmgrservice.feature.shipschedule.application.port.out.ShipScheduleRepository;
import com.inlaco.crewmgrservice.feature.shipschedule.domain.model.ShipSchedule;
import com.inlaco.crewmgrservice.feature.shipschedule.infrastructure.persistence.mongodb.entity.ShipScheduleEntity;
import com.inlaco.crewmgrservice.feature.shipschedule.infrastructure.persistence.mongodb.mapper.ShipScheduleEntityMapper;
import com.inlaco.crewmgrservice.feature.shipschedule.infrastructure.persistence.mongodb.repository.ShipScheduleMongoRepository;
import com.inlaco.crewmgrservice.infrastructure.persistence.mongodb.aggregation.FacetResult;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Slf4j
public class ShipScheduleRepositoryAdapter implements ShipScheduleRepository {

  private final ShipScheduleMongoRepository mongoRepository;
  private final ShipScheduleEntityMapper entityMapper;
  private final MongoTemplate mongoTemplate;

  @Override
  public ShipSchedule save(ShipSchedule shipSchedule) {
    String id = shipSchedule.getId();
    if (id == null) {
      // INSERT
      ShipScheduleEntity entity = entityMapper.toEntity(shipSchedule);
      ShipScheduleEntity saved = mongoRepository.save(entity);
      return entityMapper.toDomain(saved);
    }

    ShipScheduleEntity entity =
        mongoRepository
            .findById(id)
            .map(
                existing -> {
                  entityMapper.updateFromShipSchedule(shipSchedule, existing);
                  return existing;
                })
            .orElseGet(() -> entityMapper.toEntity(shipSchedule));
    ShipScheduleEntity saved = mongoRepository.save(entity);
    return entityMapper.toDomain(saved);
  }

  @Override
  public Optional<ShipSchedule> findById(String id) {
    return mongoRepository.findById(id).map(entityMapper::toDomain);
  }

  @Override
  public List<ShipSchedule> findByVesselOwnerId(String vesselOwnerId) {
    return mongoRepository.findByVesselOwnerId(vesselOwnerId).stream()
        .map(entityMapper::toDomain)
        .toList();
  }

  @Override
  public List<ShipSchedule> findByShipImo(String shipImo) {
    return mongoRepository.findByShipIMO(shipImo).stream().map(entityMapper::toDomain).toList();
  }

  @Override
  public List<ShipSchedule> findByDepartureTimeBetween(Instant startTime, Instant endTime) {
    return mongoRepository.findByDepartureTimeBetween(startTime, endTime).stream()
        .map(entityMapper::toDomain)
        .toList();
  }

  @Override
  public List<ShipSchedule> findByStatus(String status) {
    return mongoRepository.findByStatus(status).stream().map(entityMapper::toDomain).toList();
  }

  @Override
  public Page<ShipSchedule> findAll(Pageable pageable) {
    return mongoRepository.findAll(pageable).map(entityMapper::toDomain);
  }

  @Override
  public Page<ShipSchedule> findAll(ShipScheduleSearchCriteria criteria, Pageable pageable) {
    var query = new Criteria();

    if (criteria != null) {
      if (criteria.getVesselOwnerId() != null && !criteria.getVesselOwnerId().isEmpty()) {
        query.and("vesselOwnerId").is(criteria.getVesselOwnerId());
      }

      if (criteria.getShipIMO() != null && !criteria.getShipIMO().isEmpty()) {
        query.and("shipIMO").is(criteria.getShipIMO());
      }

      if (criteria.getStatus() != null) {
        query.and("status").is(criteria.getStatus());
      }

      if (criteria.getDepartureStartTime() != null) {
        query.and("departureTime").gte(criteria.getDepartureStartTime());
      }

      if (criteria.getDepartureEndTime() != null) {
        query.and("departureTime").lte(criteria.getDepartureEndTime());
      }

      if (criteria.getKeyword() != null && !criteria.getKeyword().trim().isEmpty()) {
        String keyword = criteria.getKeyword().trim();
        query.orOperator(
            Criteria.where("shipName").regex(keyword, "i"),
            Criteria.where("shipIMO").regex(keyword, "i"),
            Criteria.where("vesselOwnerId").regex(keyword, "i"));
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
        .aggregate(aggregation, ShipScheduleEntity.class, ShipScheduleFacetResult.class)
        .getUniqueMappedResult()
        .toPage(pageable)
        .map(entityMapper::toDomain);
  }

  static class ShipScheduleFacetResult extends FacetResult<ShipScheduleEntity> {}
}

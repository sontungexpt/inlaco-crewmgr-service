package com.inlaco.crewmgrservice.feature.shipschedule.infrastructure.persistence.mongodb.adapter;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

import com.inlaco.crewmgrservice.feature.shipschedule.application.model.ShipScheduleSearchCriteria;
import com.inlaco.crewmgrservice.feature.shipschedule.application.port.out.ShipScheduleRepository;
import com.inlaco.crewmgrservice.feature.shipschedule.domain.model.ShipSchedule;
import com.inlaco.crewmgrservice.feature.shipschedule.infrastructure.persistence.mongodb.entity.ShipScheduleCrewAssignmentEntity;
import com.inlaco.crewmgrservice.feature.shipschedule.infrastructure.persistence.mongodb.entity.ShipScheduleEntity;
import com.inlaco.crewmgrservice.feature.shipschedule.infrastructure.persistence.mongodb.mapper.ShipScheduleEntityMapper;
import com.inlaco.crewmgrservice.feature.shipschedule.infrastructure.persistence.mongodb.repository.ShipScheduleMongoRepository;
import com.inlaco.crewmgrservice.infrastructure.persistence.mongodb.aggregation.FacetResult;
import java.time.Instant;
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
import org.springframework.data.mongodb.core.query.Query;
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
    return mongoRepository.findByShipInfoImoNumber(shipImo).stream()
        .map(entityMapper::toDomain)
        .toList();
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

    List<org.springframework.data.mongodb.core.aggregation.AggregationOperation> stages =
        new ArrayList<>();

    // 1. Tách và build các điều kiện lọc thuộc bảng ShipScheduleEntity gốc
    Criteria scheduleCriteria = buildScheduleCriteria(criteria);
    stages.add(match(scheduleCriteria));

    // 2. Kiểm tra xem có cần tìm theo Crew Account Id hay không
    boolean hasCrewFilter =
        criteria != null
            && criteria.getCrewAccountId() != null
            && !criteria.getCrewAccountId().isBlank();

    if (hasCrewFilter) {
      // CHỈ thực hiện lookup khi thực sự cần lọc theo Crew
      stages.add(
          lookup(
              mongoTemplate.getCollectionName(ShipScheduleCrewAssignmentEntity.class),
              "_id",
              "scheduleId",
              "crewAssignments"));

      // Lọc chính xác bản ghi sau khi đã join mảng
      stages.add(
          match(
              Criteria.where("crewAssignments.accountId")
                  .is(new ObjectId(criteria.getCrewAccountId()))));
    }

    // 3. Công đoạn Facet phân trang cuối cùng (Luôn luôn có)
    stages.add(
        facet(Aggregation.count().as(FacetResult.COUNT_KEY))
            .as(FacetResult.COUNT_FACET_NAME)
            .and(
                sort(pageable.getSort()), skip(pageable.getOffset()), limit(pageable.getPageSize()))
            .as(FacetResult.DATA_FACET_NAME));

    // Tạo Aggregation từ danh sách các stage động
    Aggregation aggregation = newAggregation(stages);

    ShipScheduleFacetResult result =
        mongoTemplate
            .aggregate(aggregation, ShipScheduleEntity.class, ShipScheduleFacetResult.class)
            .getUniqueMappedResult();

    if (result == null) {
      return Page.empty(pageable);
    }

    return result.toPage(pageable).map(entityMapper::toDomain);
  }

  private Criteria buildScheduleCriteria(ShipScheduleSearchCriteria criteria) {
    List<Criteria> andCriteria = new ArrayList<>();

    if (criteria == null) {
      return new Criteria();
    }

    if (criteria.getVesselOwnerId() != null && !criteria.getVesselOwnerId().isBlank()) {
      andCriteria.add(Criteria.where("vesselOwnerId").is(criteria.getVesselOwnerId()));
    }

    if (criteria.getShipIMO() != null && !criteria.getShipIMO().isBlank()) {
      andCriteria.add(Criteria.where("shipInfo.imoNumber").is(criteria.getShipIMO()));
    }

    if (criteria.getStatus() != null) {
      andCriteria.add(Criteria.where("status").is(criteria.getStatus()));
    }

    if (criteria.getDepartureStartTime() != null) {
      andCriteria.add(Criteria.where("departureTime").gte(criteria.getDepartureStartTime()));
    }

    if (criteria.getDepartureEndTime() != null) {
      andCriteria.add(Criteria.where("departureTime").lte(criteria.getDepartureEndTime()));
    }

    if (criteria.getKeyword() != null && !criteria.getKeyword().trim().isBlank()) {
      String keyword = criteria.getKeyword().trim();

      andCriteria.add(
          new Criteria()
              .orOperator(
                  Criteria.where("shipInfo.name").regex(keyword, "i"),
                  Criteria.where("shipInfo.imoNumber").regex(keyword, "i"),
                  Criteria.where("route").regex(keyword, "i"),
                  Criteria.where("departurePort").regex(keyword, "i"),
                  Criteria.where("arrivalPort").regex(keyword, "i")));
    }

    if (andCriteria.isEmpty()) {
      return new Criteria();
    }

    return new Criteria().andOperator(andCriteria);
  }

  @Override
  public List<ShipSchedule> findByShipImoAndTimeOverlap(
      String shipImo, Instant startTime, Instant endTime) {

    Criteria criteria =
        new Criteria()
            .andOperator(
                Criteria.where("shipInfo.imoNumber").is(shipImo),
                Criteria.where("departureTime").lte(endTime),
                Criteria.where("arrivalTime").gte(startTime));

    Query query = new Query(criteria);

    return mongoTemplate.find(query, ShipScheduleEntity.class).stream()
        .map(entityMapper::toDomain)
        .toList();
  }

  static class ShipScheduleFacetResult extends FacetResult<ShipScheduleEntity> {}

  @Override
  public Optional<ShipSchedule> findOneByShipImoAndTimeOverlap(
      String shipImo, Instant startTime, Instant endTime) {

    Criteria criteria =
        new Criteria()
            .andOperator(
                Criteria.where("shipInfo.imoNumber").is(shipImo),
                Criteria.where("departureTime").lte(endTime),
                Criteria.where("arrivalTime").gte(startTime));

    Query query = new Query(criteria);

    var found = mongoTemplate.findOne(query, ShipScheduleEntity.class);

    if (found == null) {
      return Optional.empty();
    }
    return Optional.of(entityMapper.toDomain(found));
  }
}

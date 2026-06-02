package com.inlaco.crewmgrservice.feature.shipschedule.infrastructure.persistence.mongodb.adapter;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

import com.inlaco.crewmgrservice.feature.shipschedule.application.model.AttendanceLogSearchCriteria;
import com.inlaco.crewmgrservice.feature.shipschedule.application.port.out.AttendanceLogRepository;
import com.inlaco.crewmgrservice.feature.shipschedule.domain.model.AttendanceLog;
import com.inlaco.crewmgrservice.feature.shipschedule.infrastructure.persistence.mongodb.entity.AttendanceLogEntity;
import com.inlaco.crewmgrservice.feature.shipschedule.infrastructure.persistence.mongodb.mapper.AttendanceLogEntityMapper;
import com.inlaco.crewmgrservice.feature.shipschedule.infrastructure.persistence.mongodb.repository.AttendanceLogMongoRepository;
import com.inlaco.crewmgrservice.infrastructure.persistence.mongodb.aggregation.FacetResult;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

@Repository
@RequiredArgsConstructor
public class AttendanceLogRepositoryAdapter implements AttendanceLogRepository {

  private final AttendanceLogMongoRepository mongoRepository;

  private final AttendanceLogEntityMapper entityMapper;

  private final MongoTemplate mongoTemplate;

  @Override
  public AttendanceLog save(AttendanceLog attendanceLog) {

    AttendanceLogEntity entity = entityMapper.toEntity(attendanceLog);

    AttendanceLogEntity saved = mongoRepository.save(entity);

    return entityMapper.toDomain(saved);
  }

  @Override
  public Optional<AttendanceLog> findById(String id) {

    return mongoRepository.findById(id).map(entityMapper::toDomain);
  }

  @Override
  public List<AttendanceLog> findByCrewId(String crewId) {

    return mongoRepository.findByCrewAccountId(crewId).stream()
        .map(entityMapper::toDomain)
        .toList();
  }

  @Override
  public List<AttendanceLog> findByShipScheduleId(String shipScheduleId) {

    return mongoRepository.findByShipScheduleId(shipScheduleId).stream()
        .map(entityMapper::toDomain)
        .toList();
  }

  @Override
  public List<AttendanceLog> findByCrewIdAndShipScheduleId(String crewId, String shipScheduleId) {

    return mongoRepository.findByCrewAccountIdAndShipScheduleId(crewId, shipScheduleId).stream()
        .map(entityMapper::toDomain)
        .toList();
  }

  @Override
  public List<AttendanceLog> findByTimestampBetween(Instant startTime, Instant endTime) {

    return mongoRepository.findByTimestampBetween(startTime, endTime).stream()
        .map(entityMapper::toDomain)
        .toList();
  }

  @Override
  public List<AttendanceLog> findByCrewIdAndTimestampBetween(
      String crewId, Instant startTime, Instant endTime) {

    return mongoRepository
        .findByCrewAccountIdAndTimestampBetween(crewId, startTime, endTime)
        .stream()
        .map(entityMapper::toDomain)
        .toList();
  }

  @Override
  public List<AttendanceLog> findByShipScheduleIdAndTimestampBetween(
      String shipScheduleId, Instant startTime, Instant endTime) {

    return mongoRepository
        .findByShipScheduleIdAndTimestampBetween(shipScheduleId, startTime, endTime)
        .stream()
        .map(entityMapper::toDomain)
        .toList();
  }

  @Override
  public Optional<AttendanceLog> findLatestByEmployeeCardIdAndShipScheduleId(
      String employeeCardId, String shipScheduleId) {

    return mongoRepository
        .findTopByCrewEmployeeCardIdAndShipScheduleIdOrderByTimestampDesc(
            employeeCardId, shipScheduleId)
        .map(entityMapper::toDomain);
  }

  @Override
  public Page<AttendanceLog> findAll(Pageable pageable) {

    return mongoRepository.findAll(pageable).map(entityMapper::toDomain);
  }

  @Override
  public void deleteById(String id) {
    mongoRepository.deleteById(id);
  }

  @Override
  public boolean existsById(String id) {
    return mongoRepository.existsById(id);
  }

  @Override
  public Optional<AttendanceLog> findLastByDeviceIdInShipSchedule(
      String deviceId, String shipScheduleId) {
    return mongoRepository
        .findTopByDeviceIdAndShipScheduleIdOrderByTimestampDesc(deviceId, shipScheduleId)
        .map(entityMapper::toDomain);
  }

  @Override
  public Page<AttendanceLog> findAll(AttendanceLogSearchCriteria criteria, Pageable pageable) {

    Criteria baseCriteria = new Criteria();

    List<Criteria> ands = new ArrayList<>();

    // shipScheduleId
    if (criteria != null && criteria.getShipScheduleId() != null) {
      ands.add(Criteria.where("shipScheduleId").is(criteria.getShipScheduleId()));
    }

    // crewAccountId
    if (criteria != null && criteria.getCrewAccountId() != null) {
      ands.add(Criteria.where("crewAccountId").is(criteria.getCrewAccountId()));
    }

    // checkType
    if (criteria != null && criteria.getCheckType() != null) {
      ands.add(Criteria.where("checkType").is(criteria.getCheckType()));
    }

    // time range
    if (criteria != null) {
      if (criteria.getStartTime() != null && criteria.getEndTime() != null) {
        ands.add(
            Criteria.where("timestamp").gte(criteria.getStartTime()).lte(criteria.getEndTime()));
      } else if (criteria.getStartTime() != null) {
        ands.add(Criteria.where("timestamp").gte(criteria.getStartTime()));
      } else if (criteria.getEndTime() != null) {
        ands.add(Criteria.where("timestamp").lte(criteria.getEndTime()));
      }
    }

    // keyword (search crewName / note / deviceId)
    if (criteria != null && StringUtils.hasText(criteria.getKeyword())) {
      String keyword = criteria.getKeyword();

      ands.add(
          new Criteria()
              .orOperator(
                  Criteria.where("crewName").regex(keyword, "i"),
                  Criteria.where("note").regex(keyword, "i"),
                  Criteria.where("deviceId").regex(keyword, "i")));
    }

    if (!ands.isEmpty()) {
      baseCriteria = new Criteria().andOperator(ands);
    }

    Aggregation aggregation =
        newAggregation(
            match(baseCriteria),
            facet(count().as(FacetResult.COUNT_KEY))
                .as(FacetResult.COUNT_FACET_NAME)
                .and(
                    sort(pageable.getSort()),
                    skip(pageable.getOffset()),
                    limit(pageable.getPageSize()))
                .as(FacetResult.DATA_FACET_NAME));

    var result =
        mongoTemplate
            .aggregate(aggregation, AttendanceLogEntity.class, AttendanceLogFacetResult.class)
            .getUniqueMappedResult();

    if (result == null) {
      return Page.empty(pageable);
    }

    return result.toPage(pageable).map(entityMapper::toDomain);
  }

  static class AttendanceLogFacetResult extends FacetResult<AttendanceLogEntity> {}
}

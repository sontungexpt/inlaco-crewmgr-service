package com.inlaco.crewmgrservice.feature.shipschedule.infrastructure.persistence.mongodb.adapter;

import com.inlaco.crewmgrservice.feature.shipschedule.application.port.out.ShipScheduleCrewAssignmentRepository;
import com.inlaco.crewmgrservice.feature.shipschedule.domain.model.ShipScheduleCrewAssignment;
import com.inlaco.crewmgrservice.feature.shipschedule.infrastructure.persistence.mongodb.entity.ShipScheduleCrewAssignmentEntity;
import com.inlaco.crewmgrservice.feature.shipschedule.infrastructure.persistence.mongodb.mapper.ShipScheduleCrewAssignmentEntityMapper;
import com.inlaco.crewmgrservice.feature.shipschedule.infrastructure.persistence.mongodb.repository.ShipScheduleCrewAssignmentMongoRepository;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.BulkOperations.BulkMode;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Slf4j
public class ShipScheduleCrewAssignmentRepositoryAdapter
    implements ShipScheduleCrewAssignmentRepository {

  private final ShipScheduleCrewAssignmentEntityMapper mapper;
  private final ShipScheduleCrewAssignmentMongoRepository repository;
  private final MongoTemplate mongoTemplate;

  @Override
  public ShipScheduleCrewAssignment save(ShipScheduleCrewAssignment shipScheduleCrewAssignment) {
    String id = shipScheduleCrewAssignment.getId();
    if (id == null) {
      return mapper.toShipScheduleCrewAssignment(
          repository.insert(mapper.toShipScheduleCrewAssignmentEntity(shipScheduleCrewAssignment)));
    }
    ShipScheduleCrewAssignmentEntity entity =
        repository
            .findById(id)
            .map(
                existing -> {
                  mapper.updateFromShipScheduleCrewAssignment(shipScheduleCrewAssignment, existing);
                  return existing;
                })
            .orElseGet(() -> mapper.toShipScheduleCrewAssignmentEntity(shipScheduleCrewAssignment));
    return mapper.toShipScheduleCrewAssignment(repository.save(entity));
  }

  @Override
  public List<ShipScheduleCrewAssignment> findByScheduleId(String shipScheduleId) {
    return repository.findByScheduleId(new ObjectId(shipScheduleId)).stream()
        .map(mapper::toShipScheduleCrewAssignment)
        .toList();
  }

  @Override
  public Optional<ShipScheduleCrewAssignment> findByAccountIdAndScheduleId(
      String accountId, String shipScheduleId) {
    return repository
        .findByAccountIdAndScheduleId(new ObjectId(accountId), new ObjectId(shipScheduleId))
        .map(mapper::toShipScheduleCrewAssignment);
  }

  @Override
  public List<ShipScheduleCrewAssignment> saveAll(
      Iterable<ShipScheduleCrewAssignment> assignments) {
    if (assignments == null) return Collections.emptyList();
    Streamable<ShipScheduleCrewAssignment> source = Streamable.of(assignments);
    if (source.isEmpty()) return Collections.emptyList();

    List<ShipScheduleCrewAssignmentEntity> newEntities = new ArrayList<>();
    List<ShipScheduleCrewAssignment> upadateShipScheduleCrewAssignment = new ArrayList<>();
    source.stream()
        .forEach(
            notification -> {
              String id = notification.getId();
              if (id == null) {
                newEntities.add(mapper.toShipScheduleCrewAssignmentEntity(notification));
              } else {
                upadateShipScheduleCrewAssignment.add(notification);
              }
            });

    if (upadateShipScheduleCrewAssignment.isEmpty()) {
      return mongoTemplate.insert(newEntities, ShipScheduleCrewAssignmentEntity.class).stream()
          .map(mapper::toShipScheduleCrewAssignment)
          .toList();
    }

    List<String> resultIds =
        upadateShipScheduleCrewAssignment.stream()
            .map(ShipScheduleCrewAssignment::getId)
            .collect(Collectors.toList());

    Map<String, ShipScheduleCrewAssignmentEntity> existingMap =
        repository.findAllById(resultIds).stream()
            .collect(
                Collectors.toMap(ShipScheduleCrewAssignmentEntity::getId, Function.identity()));

    BulkOperations bulkOps =
        mongoTemplate.bulkOps(BulkMode.UNORDERED, ShipScheduleCrewAssignmentEntity.class);
    if (!newEntities.isEmpty()) {
      bulkOps.insert(newEntities);
    }

    for (var assignment : upadateShipScheduleCrewAssignment) {
      String id = assignment.getId();
      var existing = existingMap.get(id);
      if (existing == null) {
        bulkOps.insert(mapper.toShipScheduleCrewAssignmentEntity(assignment));
      } else {
        mapper.updateFromShipScheduleCrewAssignment(assignment, existing);
        bulkOps.replaceOne(Query.query(Criteria.where("_id").is(id)), existing);
      }
    }
    bulkOps
        .execute()
        .getInserts()
        .forEach(r -> resultIds.add(r.getId().asObjectId().getValue().toHexString()));

    return repository.findAllById(resultIds).stream()
        .map(mapper::toShipScheduleCrewAssignment)
        .toList();
  }

  @Override
  public List<ShipScheduleCrewAssignment> findByProfileIdAndTimeRangeOverlap(
      String profileId, Instant startDate, Instant endDate) {

    Criteria criteria =
        Criteria.where("profileId")
            .is(new ObjectId(profileId))
            .and("boardingTime")
            .lte(endDate)
            .and("disembarkTime")
            .gte(startDate);

    Query query =
        new Query(criteria)
            // Sort by boarding time for timetable rendering
            .with(Sort.by(Sort.Direction.ASC, "boardingTime"));

    return mongoTemplate.find(query, ShipScheduleCrewAssignmentEntity.class).stream()
        .map(mapper::toShipScheduleCrewAssignment)
        .toList();
  }

  @Override
  public List<ShipScheduleCrewAssignment> findByProfileIdFullyInTimeRange(
      String profileId, Instant startDate, Instant endDate) {

    Criteria criteria =
        Criteria.where("profileId")
            .is(new ObjectId(profileId))
            .and("boardingTime")
            .gte(startDate)
            .and("disembarkTime")
            .lte(endDate);

    Query query =
        new Query(criteria)
            // Sort by start time for schedule ordering
            .with(Sort.by(Sort.Direction.ASC, "boardingTime"));

    return mongoTemplate.find(query, ShipScheduleCrewAssignmentEntity.class).stream()
        .map(mapper::toShipScheduleCrewAssignment)
        .toList();
  }

  @Override
  public List<ShipScheduleCrewAssignment> findByTimeRangeOverlap(
      Instant startTime, Instant endTime) {

    Criteria criteria =
        Criteria.where("boardingTime").lte(endTime).and("disembarkTime").gte(startTime);

    Query query =
        new Query(criteria)
            // Sort by start time for schedule ordering
            .with(Sort.by(Sort.Direction.ASC, "boardingTime"));
    return mongoTemplate.find(query, ShipScheduleCrewAssignmentEntity.class).stream()
        .map(mapper::toShipScheduleCrewAssignment)
        .toList();
  }

  @Override
  public boolean existsProfileIdAndTimeRangeOverlap(
      String profileId, Instant startDate, Instant endDate) {

    Criteria criteria =
        Criteria.where("profileId")
            .is(new ObjectId(profileId))
            .and("boardingTime")
            .lte(endDate)
            .and("disembarkTime")
            .gte(startDate);
    Query query = new Query(criteria);
    return mongoTemplate.exists(query, ShipScheduleCrewAssignmentEntity.class);
  }
}

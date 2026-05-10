package com.inlaco.crewmgrservice.feature.crewmobilization.infrastructure.persistence.mongodb.adapter;

import com.inlaco.crewmgrservice.feature.crewmobilization.application.port.out.CrewMobilizationAssignmentRepository;
import com.inlaco.crewmgrservice.feature.crewmobilization.domain.model.CrewMobilizationAssignment;
import com.inlaco.crewmgrservice.feature.crewmobilization.infrastructure.persistence.mongodb.entity.CrewMobilizationAssignmentEntity;
import com.inlaco.crewmgrservice.feature.crewmobilization.infrastructure.persistence.mongodb.mapper.CrewMobilizationAssignmentEntityMapper;
import com.inlaco.crewmgrservice.feature.crewmobilization.infrastructure.persistence.mongodb.repository.CrewMobilizationAssignmentMongoRepository;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.BulkOperations.BulkMode;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CrewMobilizationAssignmentRepositoryAdapter
    implements CrewMobilizationAssignmentRepository {

  private final CrewMobilizationAssignmentMongoRepository repository;

  private final CrewMobilizationAssignmentEntityMapper mapper;

  private final MongoTemplate mongoTemplate;

  @Override
  public CrewMobilizationAssignment save(CrewMobilizationAssignment assignment) {
    String id = assignment.getId();
    if (id == null) {
      return mapper.toDomain(repository.insert(mapper.toEntity(assignment)));
    }
    CrewMobilizationAssignmentEntity entity =
        repository
            .findById(id)
            .map(
                existing -> {
                  mapper.updateFromCrewMobilizationAssignment(assignment, existing);
                  return existing;
                })
            .orElseGet(() -> mapper.toEntity(assignment));
    return mapper.toDomain(repository.save(entity));
  }

  @Override
  public List<CrewMobilizationAssignment> saveAll(
      Iterable<CrewMobilizationAssignment> assignments) {
    if (assignments == null) return Collections.emptyList();
    Streamable<CrewMobilizationAssignment> source = Streamable.of(assignments);
    if (source.isEmpty()) return Collections.emptyList();

    List<CrewMobilizationAssignmentEntity> newEntities = new ArrayList<>();
    List<CrewMobilizationAssignment> updateCrewMobilizationAssignment = new ArrayList<>();
    source.stream()
        .forEach(
            notification -> {
              String id = notification.getId();
              if (id == null) {
                newEntities.add(mapper.toEntity(notification));
              } else {
                updateCrewMobilizationAssignment.add(notification);
              }
            });

    if (updateCrewMobilizationAssignment.isEmpty()) {
      return mongoTemplate.insert(newEntities, CrewMobilizationAssignmentEntity.class).stream()
          .map(mapper::toDomain)
          .toList();
    }

    List<String> resultIds =
        updateCrewMobilizationAssignment.stream()
            .map(CrewMobilizationAssignment::getId)
            .collect(Collectors.toList());

    Map<String, CrewMobilizationAssignmentEntity> existingMap =
        repository.findAllById(resultIds).stream()
            .collect(
                Collectors.toMap(CrewMobilizationAssignmentEntity::getId, Function.identity()));

    BulkOperations bulkOps =
        mongoTemplate.bulkOps(BulkMode.UNORDERED, CrewMobilizationAssignmentEntity.class);
    if (!newEntities.isEmpty()) {
      bulkOps.insert(newEntities);
    }

    for (CrewMobilizationAssignment assignment : updateCrewMobilizationAssignment) {
      String id = assignment.getId();
      CrewMobilizationAssignmentEntity existing = existingMap.get(id);
      if (existing == null) {
        bulkOps.insert(mapper.toEntity(assignment));
      } else {
        mapper.updateFromCrewMobilizationAssignment(assignment, existing);
        bulkOps.replaceOne(Query.query(Criteria.where("_id").is(id)), existing);
      }
    }
    bulkOps
        .execute()
        .getInserts()
        .forEach(r -> resultIds.add(r.getId().asObjectId().getValue().toHexString()));

    return repository.findAllById(resultIds).stream().map(mapper::toDomain).toList();
  }

  @Override
  public Optional<CrewMobilizationAssignment> findById(String id) {
    return repository.findById(id).map(mapper::toDomain);
  }

  @Override
  public List<CrewMobilizationAssignment> findByMobilizationId(String mobilizationId) {
    return repository.findByMobilizationId(new ObjectId(mobilizationId)).stream()
        .map(mapper::toDomain)
        .toList();
  }

  @Override
  public List<CrewMobilizationAssignment> findByProfileId(String profileId) {

    return repository.findByProfileId(new ObjectId(profileId)).stream()
        .map(mapper::toDomain)
        .toList();
  }

  @Override
  public boolean existsAssignmentOverlap(String profileId, Instant startDate, Instant endDate) {

    Criteria criteria =
        Criteria.where("profileId")
            .is(new ObjectId(profileId))
            .and("startDate")
            .lte(endDate)
            .and("endDate")
            .gte(startDate);

    Query query = new Query(criteria);

    return mongoTemplate.exists(query, CrewMobilizationAssignmentEntity.class);
  }

  @Override
  public List<CrewMobilizationAssignment> findNonEndedAssignmentsByEmployeeCardIds(
      Iterable<String> employeeCardIds) {
    return repository
        .findByEmployeeCardIdInAndEndDateGreaterThan(employeeCardIds, Instant.now())
        .stream()
        .map(mapper::toDomain)
        .toList();
  }
}

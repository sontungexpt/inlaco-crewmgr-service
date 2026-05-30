package com.inlaco.crewmgrservice.feature.crewmobilization.infrastructure.persistence.mongodb.adapter;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

import com.inlaco.crewmgrservice.feature.crewmobilization.application.port.out.CrewMobilizationAssignmentRepository;
import com.inlaco.crewmgrservice.feature.crewmobilization.domain.model.CrewMobilizationAssignment;
import com.inlaco.crewmgrservice.feature.crewmobilization.infrastructure.persistence.mongodb.entity.CrewMobilizationAssignmentEntity;
import com.inlaco.crewmgrservice.feature.crewmobilization.infrastructure.persistence.mongodb.entity.CrewMobilizationEntity;
import com.inlaco.crewmgrservice.feature.crewmobilization.infrastructure.persistence.mongodb.mapper.CrewMobilizationAssignmentEntityMapper;
import com.inlaco.crewmgrservice.feature.crewmobilization.infrastructure.persistence.mongodb.repository.CrewMobilizationAssignmentMongoRepository;
import com.inlaco.crewmgrservice.infrastructure.persistence.mongodb.aggregation.FacetResult;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.*;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.BulkOperations.BulkMode;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
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

  @Override
  public List<CrewMobilizationAssignment> findAllActiveAssignments() {
    Instant now = Instant.now();

    Query query = new Query(Criteria.where("startDate").lte(now).and("endDate").gte(now));

    return mongoTemplate.find(query, CrewMobilizationAssignmentEntity.class).stream()
        .map(mapper::toDomain)
        .toList();
  }

  @Override
  public List<CrewMobilizationAssignment> findByProfileId(
      String profileId, Instant startDate, Instant endDate) {

    Criteria criteria =
        Criteria.where("profileId")
            .is(new ObjectId(profileId))
            // overlap condition
            .and("startDate")
            .lte(endDate)
            .and("endDate")
            .gte(startDate);

    Query query = new Query(criteria).with(Sort.by(Sort.Direction.ASC, "startDate"));

    return mongoTemplate.find(query, CrewMobilizationAssignmentEntity.class).stream()
        .map(mapper::toDomain)
        .toList();
  }

  @Override
  public List<CrewMobilizationAssignment> findAllActiveAssignmentsForClient(String clientId) {

    Instant now = Instant.now();

    Aggregation aggregation =
        Aggregation.newAggregation(
            Aggregation.lookup(
                mongoTemplate.getCollectionName(CrewMobilizationEntity.class),
                "mobilizationId",
                "_id",
                "mobilization"),
            Aggregation.unwind("mobilization"),
            Aggregation.match(
                new Criteria()
                    .andOperator(
                        Criteria.where("mobilization.partnerAccountId").is(clientId),
                        Criteria.where("startDate").lte(now),
                        Criteria.where("endDate").gte(now))));

    AggregationResults<CrewMobilizationAssignmentEntity> results =
        mongoTemplate.aggregate(
            aggregation,
            CrewMobilizationAssignmentEntity.class,
            CrewMobilizationAssignmentEntity.class);

    return results.getMappedResults().stream().map(mapper::toDomain).toList();
  }

  public Page<CrewMobilizationAssignment> findAllActiveAssignmentsForClient(
      String clientId, Pageable pageable) {

    Instant now = Instant.now();

    List<AggregationOperation> operations = new ArrayList<>();

    /*
     * Active assignment
     */
    operations.add(match(Criteria.where("startDate").lte(now).and("endDate").gte(now)));

    /*
     * Join mobilization
     */
    operations.add(
        lookup(
            mongoTemplate.getCollectionName(CrewMobilizationEntity.class),
            "mobilizationId",
            "_id",
            "mobilization"));

    operations.add(unwind("mobilization"));

    /*
     * Filter by client
     */
    operations.add(match(Criteria.where("mobilization.partnerAccountId").is(clientId)));

    /*
     * Pagination
     */
    operations.add(
        facet(Aggregation.count().as(FacetResult.COUNT_KEY))
            .as(FacetResult.COUNT_FACET_NAME)
            .and(
                sort(pageable.getSort()), skip(pageable.getOffset()), limit(pageable.getPageSize()))
            .as(FacetResult.DATA_FACET_NAME));

    Aggregation aggregation = newAggregation(operations);

    CrewMobilizationAssignmentFacetResult result =
        mongoTemplate
            .aggregate(
                aggregation,
                CrewMobilizationAssignmentEntity.class,
                CrewMobilizationAssignmentFacetResult.class)
            .getUniqueMappedResult();

    if (result == null) {
      return Page.empty(pageable);
    }

    return result.toPage(pageable).map(mapper::toDomain);
  }

  static class CrewMobilizationAssignmentFacetResult
      extends FacetResult<CrewMobilizationAssignmentEntity> {}
}

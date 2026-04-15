package com.inlaco.crewmgrservice.feature.crew.infrastructure.persistence.mongodb.adapter;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

import com.inlaco.crewmgrservice.feature.crew.application.model.CrewProfileSearchCriteria;
import com.inlaco.crewmgrservice.feature.crew.application.port.out.CrewProfileRepository;
import com.inlaco.crewmgrservice.feature.crew.domain.model.CrewProfile;
import com.inlaco.crewmgrservice.feature.crew.infrastructure.persistence.mongodb.entity.CrewProfileEntity;
import com.inlaco.crewmgrservice.feature.crew.infrastructure.persistence.mongodb.mapper.CrewProfileEntityMapper;
import com.inlaco.crewmgrservice.feature.crew.infrastructure.persistence.mongodb.repository.CrewProfileMongoRepository;
import com.inlaco.crewmgrservice.infrastructure.persistence.mongodb.aggregation.FacetResult;
import com.inlaco.crewmgrservice.infrastructure.persistence.support.PageableUtils;
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
import org.jspecify.annotations.Nullable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.BulkOperations.BulkMode;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

@Repository
@RequiredArgsConstructor
@Slf4j
public class CrewProfileRepositoryAdapter implements CrewProfileRepository {
  private final CrewProfileMongoRepository repository;
  private final CrewProfileEntityMapper mapper;
  private final MongoOperations mongoOperations;

  @Override
  public Optional<CrewProfile> findById(String profileId) {
    return repository.findById(profileId).map(mapper::toCrewProfile);
  }

  @Override
  @Cacheable(value = "crew-profiles", key = "'account:' + #accountId")
  public Optional<CrewProfile> findByAccountId(String accountId) {
    return repository.findByAccountId(new ObjectId(accountId)).map(mapper::toCrewProfile);
  }

  @Override
  @Cacheable(value = "crew-profiles", key = "'card:' + #cardId")
  public Optional<CrewProfile> findByEmployeeCardId(String cardId) {
    return repository.findByEmployeeCardId(cardId).map(mapper::toCrewProfile);
  }

  @Override
  public List<CrewProfile> findAllByEmployeeCardId(Iterable<String> cardIds) {
    return repository.findByEmployeeCardIdIn(cardIds).stream().map(mapper::toCrewProfile).toList();
  }

  @Override
  @Caching(
      evict = {
        @CacheEvict(
            value = "crew-profiles",
            key = "'account:' + #profile.accountId",
            condition = "#profile != null && #profile.accountId != null"),
        @CacheEvict(
            value = "crew-profiles",
            key = "'card:' + #profile.employeeCardId",
            condition = "#profile != null && #profile.employeeCardId != null"),
      })
  public CrewProfile save(CrewProfile profile) {
    String id = profile.getId();
    if (id == null) {
      // INSERT
      return mapper.toCrewProfile(repository.insert(mapper.toCrewProfileEntity(profile)));
    }
    CrewProfileEntity entity =
        repository
            .findById(id)
            .map(
                existing -> {
                  mapper.updateFromCrewProfile(profile, existing);
                  return existing;
                })
            .orElseGet(() -> mapper.toCrewProfileEntity(profile));
    return mapper.toCrewProfile(repository.save(entity));
  }

  @CacheEvict(value = "crew-profiles", allEntries = true)
  public List<CrewProfile> saveAll(Iterable<CrewProfile> profiles) {
    if (profiles == null) return Collections.emptyList();
    Streamable<CrewProfile> source = Streamable.of(profiles);
    if (source.isEmpty()) return Collections.emptyList();

    List<CrewProfileEntity> newEntities = new ArrayList<>();
    List<CrewProfile> updateProfiles = new ArrayList<>();
    source.stream()
        .forEach(
            profile -> {
              String id = profile.getId();
              if (id == null) {
                newEntities.add(mapper.toCrewProfileEntity(profile));
              } else {
                updateProfiles.add(profile);
              }
            });

    if (updateProfiles.isEmpty()) {
      return mongoOperations.insert(newEntities, CrewProfileEntity.class).stream()
          .map(mapper::toCrewProfile)
          .toList();
    }

    List<String> resultIds =
        updateProfiles.stream().map(CrewProfile::getId).collect(Collectors.toList());

    Map<String, CrewProfileEntity> existingMap =
        repository.findAllById(resultIds).stream()
            .collect(Collectors.toMap(CrewProfileEntity::getId, Function.identity()));

    BulkOperations bulkOps = mongoOperations.bulkOps(BulkMode.UNORDERED, CrewProfileEntity.class);
    if (!newEntities.isEmpty()) {
      bulkOps.insert(newEntities);
    }

    for (CrewProfile profile : updateProfiles) {
      String id = profile.getId();
      CrewProfileEntity existing = existingMap.get(id);
      if (existing == null) {
        bulkOps.insert(mapper.toCrewProfileEntity(profile));
      } else {
        mapper.updateFromCrewProfile(profile, existing);
        bulkOps.replaceOne(Query.query(Criteria.where("_id").is(id)), existing);
      }
    }
    bulkOps
        .execute()
        .getInserts()
        .forEach(r -> resultIds.add(r.getId().asObjectId().getValue().toHexString()));

    return repository.findAllById(resultIds).stream().map(mapper::toCrewProfile).toList();
  }

  @Override
  public Page<CrewProfile> findAll(Pageable pageable) {
    return repository.findAll(pageable).map(mapper::toCrewProfile);
  }

  @Override
  public Page<CrewProfile> findAll(
      @Nullable CrewProfileSearchCriteria criteria, Pageable pageable) {

    pageable = PageableUtils.enforceIdSort(pageable);

    List<Criteria> andConditions = new ArrayList<>();

    if (criteria != null) {
      if (StringUtils.hasText(criteria.keyword())) {
        log.debug("Filter by keyword: {}", criteria.keyword());

        String keyword = criteria.keyword().trim();

        Criteria keywordCriteria =
            new Criteria()
                .orOperator(
                    Criteria.where("employeeCardId").regex(keyword, "i"),
                    Criteria.where("phoneNumber").regex(keyword, "i"),
                    Criteria.where("fullName").regex(keyword, "i"),
                    Criteria.where("email").regex(keyword, "i"));

        andConditions.add(keywordCriteria);
      }

      if (criteria.official() != null) {
        log.debug("Filter by official: {}", criteria.official());
        if (criteria.official()) {
          andConditions.add(Criteria.where("employeeCardId").ne(null).ne(""));
        } else {
          andConditions.add(
              new Criteria()
                  .orOperator(
                      Criteria.where("employeeCardId").is(null),
                      Criteria.where("employeeCardId").exists(false)));
        }
      }

      if (criteria.workStatus() != null) {
        log.debug("Filter by workStatus: {}", criteria.workStatus());
        andConditions.add(Criteria.where("status").is(criteria.workStatus()));
      }

      if (criteria.professionalPosition() != null) {
        log.debug("Filter by professionalPosition: {}", criteria.professionalPosition());

        andConditions.add(
            Criteria.where("professionalPosition").is(criteria.professionalPosition()));
      }
    }

    Criteria finalCriteria = new Criteria();
    if (!andConditions.isEmpty()) {
      finalCriteria.andOperator(andConditions.toArray(new Criteria[0]));
    }

    Aggregation aggregation =
        newAggregation(
            match(finalCriteria),
            facet(Aggregation.count().as(FacetResult.COUNT_KEY))
                .as(FacetResult.COUNT_FACET_NAME)
                .and(
                    sort(pageable.getSort()),
                    skip(pageable.getOffset()),
                    limit(pageable.getPageSize()))
                .as(FacetResult.DATA_FACET_NAME));

    return mongoOperations
        .aggregate(aggregation, CrewProfileEntity.class, CrewProfileEntityFacetResult.class)
        .getUniqueMappedResult()
        .toPage(pageable)
        .map(mapper::toCrewProfile);
  }

  static class CrewProfileEntityFacetResult extends FacetResult<CrewProfileEntity> {}

  @Override
  public List<CrewProfile> findAllById(Iterable<String> ids) {
    return repository.findAllById(ids).stream().map(mapper::toCrewProfile).toList();
  }

  @Override
  public List<CrewProfile> findAllByAccountId(Iterable<String> accountIds) {
    return repository.findByAccountIdIn(accountIds).stream().map(mapper::toCrewProfile).toList();
  }

  public void deleteAll() {
    repository.deleteAll();
  }

  public long count() {
    return repository.count();
  }
}

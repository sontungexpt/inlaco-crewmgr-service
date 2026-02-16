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
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

@Repository
@RequiredArgsConstructor
@Slf4j
public class CrewProfileRepositoryAdapter implements CrewProfileRepository {
  private final CrewProfileMongoRepository repository;
  private final CrewProfileEntityMapper mapper;
  private final MongoTemplate mongoTemplate;

  @Override
  public Optional<CrewProfile> findById(String profileId) {
    return repository.findById(profileId).map(mapper::toCrewProfile);
  }

  @Override
  public Optional<CrewProfile> findByAccountId(String accountId) {
    return repository.findByAccountId(new ObjectId(accountId)).map(mapper::toCrewProfile);
  }

  @Override
  public Optional<CrewProfile> findByEmployeeCardId(String cardId) {
    return repository.findByEmployeeCardId(cardId).map(mapper::toCrewProfile);
  }

  @Override
  public List<CrewProfile> findAllByEmployeeCardId(Iterable<String> cardIds) {
    return repository.findByEmployeeCardIdIn(cardIds).stream().map(mapper::toCrewProfile).toList();
  }

  @Override
  public CrewProfile save(CrewProfile profile) {
    CrewProfileEntity entity;
    String id = profile.getId();
    if (id == null) {
      // INSERT
      entity = mapper.toCrewProfileEntity(profile);
    } else {
      entity =
          repository
              .findById(id)
              .map(
                  existing -> {
                    mapper.updateFromCrewProfile(profile, existing);
                    return existing;
                  })
              .orElseGet(() -> mapper.toCrewProfileEntity(profile));
    }
    return mapper.toCrewProfile(repository.save(entity));
  }

  @Override
  public List<CrewProfile> saveAll(Iterable<CrewProfile> profiles) {
    if (profiles == null) {
      return Collections.emptyList();
    }

    List<CrewProfile> domainList = StreamSupport.stream(profiles.spliterator(), false).toList();

    if (domainList.isEmpty()) {
      return Collections.emptyList();
    }

    // Collect ids for batch lookup
    List<String> ids =
        domainList.stream().map(CrewProfile::getId).filter(Objects::nonNull).toList();

    Map<String, CrewProfileEntity> existingMap =
        repository.findAllById(ids).stream()
            .collect(Collectors.toMap(CrewProfileEntity::getId, Function.identity()));

    List<CrewProfileEntity> entitiesToSave =
        domainList.stream()
            .map(
                profile -> {
                  if (profile.getId() == null) {
                    return mapper.toCrewProfileEntity(profile); // new insert
                  }

                  CrewProfileEntity existing = existingMap.get(profile.getId());

                  if (existing == null) {
                    return mapper.toCrewProfileEntity(profile); // treat as insert
                  }

                  mapper.updateFromCrewProfile(profile, existing); // merge
                  return existing;
                })
            .toList();

    return repository.saveAll(entitiesToSave).stream().map(mapper::toCrewProfile).toList();
  }

  @Override
  public Page<CrewProfile> findAll(Pageable pageable) {
    return repository.findAll(pageable).map(mapper::toCrewProfile);
  }

  @Override
  public Page<CrewProfile> findAll(
      @Nullable CrewProfileSearchCriteria criteria, Pageable pageable) {
    pageable = PageableUtils.extendDefaultSort(pageable);
    var query = new Criteria();

    if (criteria != null) {
      if (StringUtils.hasText(criteria.keyword())) {
        query.orOperator(
            Criteria.where("cardId").regex(criteria.keyword(), "i"),
            Criteria.where("phone").regex(criteria.keyword(), "i"),
            Criteria.where("fullName").regex(criteria.keyword(), "i"),
            Criteria.where("email").regex(criteria.keyword(), "i"));
      }

      if (criteria.official() != null) {
        query.and("employeeCardId").exists(criteria.official());
      }
      if (criteria.workStatus() != null) {
        query.and("workStatus").is(criteria.workStatus());
      }
      if (criteria.professionalPosition() != null) {
        query.and("professionalPosition").is(criteria.professionalPosition());
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
}

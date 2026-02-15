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
import java.util.List;
import java.util.Optional;
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
  private final CrewProfileMongoRepository crewProfileMongoRepository;
  private final CrewProfileEntityMapper crewProfileEntityMapper;
  private final MongoTemplate mongoTemplate;

  @Override
  public Optional<CrewProfile> findById(String profileId) {
    return crewProfileMongoRepository
        .findById(profileId)
        .map(crewProfileEntityMapper::toCrewProfile);
  }

  @Override
  public Optional<CrewProfile> findByAccountId(String accountId) {
    return crewProfileMongoRepository
        .findByAccountId(new ObjectId(accountId))
        .map(crewProfileEntityMapper::toCrewProfile);
  }

  @Override
  public Optional<CrewProfile> findByEmployeeCardId(String cardId) {
    return crewProfileMongoRepository
        .findByEmployeeCardId(cardId)
        .map(crewProfileEntityMapper::toCrewProfile);
  }

  @Override
  public List<CrewProfile> findByEmployeeCardIdIn(Iterable<String> cardIds) {
    return crewProfileMongoRepository.findByEmployeeCardIdIn(cardIds).stream()
        .map(crewProfileEntityMapper::toCrewProfile)
        .toList();
  }

  @Override
  public CrewProfile save(CrewProfile profile) {
    return crewProfileEntityMapper.toCrewProfile(
        crewProfileMongoRepository.save(crewProfileEntityMapper.toCrewProfileEntity(profile)));
  }

  @Override
  public Page<CrewProfile> findAll(Pageable pageable) {
    return crewProfileMongoRepository.findAll(pageable).map(crewProfileEntityMapper::toCrewProfile);
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
        .map(crewProfileEntityMapper::toCrewProfile);
  }

  static class CrewProfileEntityFacetResult extends FacetResult<CrewProfileEntity> {}

  @Override
  public List<CrewProfile> findByIdIn(Iterable<String> ids) {
    return crewProfileMongoRepository.findByIdIn(ids).stream()
        .map(crewProfileEntityMapper::toCrewProfile)
        .toList();
  }
}

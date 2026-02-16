package com.inlaco.crewmgrservice.feature.crewrental.infrastructure.persistence.mongodb.adapter;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

import com.inlaco.crewmgrservice.feature.crewrental.application.model.CrewRentalRequestSearchCriteria;
import com.inlaco.crewmgrservice.feature.crewrental.application.port.out.CrewRentalRequestRepository;
import com.inlaco.crewmgrservice.feature.crewrental.domain.model.CrewRentalRequest;
import com.inlaco.crewmgrservice.feature.crewrental.infrastructure.persistence.mongodb.entity.CrewRentalRequestEntity;
import com.inlaco.crewmgrservice.feature.crewrental.infrastructure.persistence.mongodb.mapper.CrewRentalRequestEntityMapper;
import com.inlaco.crewmgrservice.feature.crewrental.infrastructure.persistence.mongodb.repository.CrewRentalRequestMongoRepository;
import com.inlaco.crewmgrservice.infrastructure.persistence.mongodb.aggregation.FacetResult;
import com.inlaco.crewmgrservice.shared.constant.PhoneNumberRegexp;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
public class CrewRentalRequestRepositoryAdapter implements CrewRentalRequestRepository {
  private final CrewRentalRequestMongoRepository repository;
  private final CrewRentalRequestEntityMapper mapper;
  private final MongoTemplate mongoTemplate;

  @Override
  public Optional<CrewRentalRequest> findById(String id) {
    return repository.findById(id).map(mapper::toCrewRentalRequest);
  }

  @Override
  public Page<CrewRentalRequest> findAll(Pageable pageable) {
    return repository.findAll(pageable).map(mapper::toCrewRentalRequest);
  }

  @Override
  public Page<CrewRentalRequest> findAll(
      CrewRentalRequestSearchCriteria criteria, Pageable pageable) {
    var query = new Criteria();

    if (criteria != null) {
      String keyword = criteria.keyword();
      if (StringUtils.hasText(keyword)) {
        if (PhoneNumberRegexp.ITU_T_E_164.isValid(keyword)) {
          query.and("companyPhone").regex(keyword, "i");
        }
        query.and("companyEmail").regex(keyword, "i");
        query.and("companyName").regex(keyword, "i");
        query.and("shipInfo.name").regex(keyword, "i");
      }

      if (criteria.status() != null) {
        query.and("status").is(criteria.status());
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
        .aggregate(
            aggregation, CrewRentalRequestEntity.class, CrewRentalRequestEntityFacetResult.class)
        .getUniqueMappedResult()
        .toPage(pageable)
        .map(mapper::toCrewRentalRequest);
  }

  @Override
  public CrewRentalRequest save(CrewRentalRequest request) {
    CrewRentalRequestEntity entity;
    if (request.getId() == null) {
      // INSERT
      entity = mapper.toCrewRentalRequestEntity(request);
    } else {
      entity =
          repository
              .findById(request.getId())
              .map(
                  existing -> {
                    mapper.updateFromCrewRentalRequest(request, existing);
                    return existing;
                  })
              .orElseGet(() -> mapper.toCrewRentalRequestEntity(request));
    }

    return mapper.toCrewRentalRequest(repository.save(entity));
  }

  static class CrewRentalRequestEntityFacetResult extends FacetResult<CrewRentalRequestEntity> {}
}

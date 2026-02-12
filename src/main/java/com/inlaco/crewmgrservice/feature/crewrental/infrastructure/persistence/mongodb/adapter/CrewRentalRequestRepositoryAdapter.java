package com.inlaco.crewmgrservice.feature.crewrental.infrastructure.persistence.mongodb.adapter;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

import com.inlaco.crewmgrservice.common.model.FacetResult;
import com.inlaco.crewmgrservice.feature.crewrental.application.model.CrewRentalRequestSearchCriteria;
import com.inlaco.crewmgrservice.feature.crewrental.application.port.out.CrewRentalRequestRepository;
import com.inlaco.crewmgrservice.feature.crewrental.domain.model.CrewRentalRequest;
import com.inlaco.crewmgrservice.feature.crewrental.infrastructure.persistence.mongodb.entity.CrewRentalRequestEntity;
import com.inlaco.crewmgrservice.feature.crewrental.infrastructure.persistence.mongodb.mapper.CrewRentalRequestEntityMapper;
import com.inlaco.crewmgrservice.feature.crewrental.infrastructure.persistence.mongodb.repository.CrewRentalRequestMongoRepository;
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
  private final CrewRentalRequestMongoRepository crewRentalRequestMongoRepository;
  private final CrewRentalRequestEntityMapper crewRentalRequestEntityMapper;
  private final MongoTemplate mongoTemplate;

  @Override
  public Optional<CrewRentalRequest> findById(String id) {
    return crewRentalRequestMongoRepository
        .findById(id)
        .map(crewRentalRequestEntityMapper::toCrewRentalRequest);
  }

  @Override
  public Page<CrewRentalRequest> findAll(Pageable pageable) {
    return crewRentalRequestMongoRepository
        .findAll(pageable)
        .map(crewRentalRequestEntityMapper::toCrewRentalRequest);
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
        .map(crewRentalRequestEntityMapper::toCrewRentalRequest);
  }

  @Override
  public CrewRentalRequest save(CrewRentalRequest request) {
    return crewRentalRequestEntityMapper.toCrewRentalRequest(
        crewRentalRequestMongoRepository.save(
            crewRentalRequestEntityMapper.toCrewRentalRequestEntity(request)));
  }

  static class CrewRentalRequestEntityFacetResult extends FacetResult<CrewRentalRequestEntity> {}
}

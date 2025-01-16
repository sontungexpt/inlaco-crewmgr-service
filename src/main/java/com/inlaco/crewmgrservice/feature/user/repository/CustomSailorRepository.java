package com.inlaco.crewmgrservice.feature.user.repository;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.limit;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.skip;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.sort;

import com.inlaco.crewmgrservice.common.model.FacetResult;
import com.inlaco.crewmgrservice.feature.user.dto.SailorFilterable;
import com.inlaco.crewmgrservice.feature.user.model.CandidateProfile;
import com.inlaco.crewmgrservice.feature.user.model.SailorProfile;
import com.inlaco.crewmgrservice.utils.PageableUtils;
import com.inlaco.crewmgrservice.utils.PhoneNumberValidatorUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@Slf4j
@RequiredArgsConstructor
@Repository
public class CustomSailorRepository {

  private final MongoTemplate mongoTemplate;

  public long countSailorHasCardId() {
    Query query = new Query();
    query.addCriteria(Criteria.where("cardId").exists(true).ne(""));
    return mongoTemplate.count(query, SailorProfile.class);
  }

  public Page<SailorProfile> searchSailors(
      String keyword, SailorFilterable filterable, Pageable p) {
    List<Criteria> criteriaList = new ArrayList<>();
    if (ObjectId.isValid(keyword)) {
      criteriaList.add(Criteria.where("accountId").is(new ObjectId(keyword)));
    }
    if (PhoneNumberValidatorUtils.isPotentialPhoneNumber(keyword)) {
      criteriaList.add(Criteria.where("phone").regex(keyword, "i"));
    }
    criteriaList.add(Criteria.where("fullName").regex(keyword, "i"));
    criteriaList.add(Criteria.where("email").regex(keyword, "i"));

    var query = new Criteria().orOperator(criteriaList);
    if (filterable.isFilterable()) {
      query.andOperator(buildFilterableCriteria(filterable));
    }

    var pageable = PageableUtils.extendDefaultSort(p);
    log.debug("Fetching non expired courses with pagination");

    Aggregation aggregation =
        Aggregation.newAggregation(match(query), buildPaginationOperation(pageable));

    var result =
        mongoTemplate
            .aggregate(aggregation, CandidateProfile.class, CandidateProfileFacetResult.class)
            .getUniqueMappedResult();

    return new PageImpl<>(result.getDatas(), pageable, result.getCount());
  }

  private Criteria buildFilterableCriteria(SailorFilterable filterable) {
    var criteria = new Criteria();
    if (filterable.getProfessionalPosition() != null) {
      criteria.and("professionalPosition").is(filterable.getProfessionalPosition());
    }
    if (filterable.getWorkStatus() != null) {
      criteria.and("cardId").exists(true).ne("").and("workStatus").is(filterable.getWorkStatus());
    }
    return criteria;
  }

  public AggregationOperation buildPaginationOperation(Pageable pageable) {
    return Aggregation.facet(Aggregation.count().as(FacetResult.getCountKey()))
        .as(FacetResult.getCountFacetName())
        .and(sort(pageable.getSort()), skip(pageable.getOffset()), limit(pageable.getPageSize()))
        .as(FacetResult.getDataFacetName());
  }

  public Page<SailorProfile> fetchAllSailors(SailorFilterable filterable, Pageable p) {
    var pageable = PageableUtils.extendDefaultSort(p);
    log.debug("Fetching non expired courses with pagination");
    List<AggregationOperation> operations = new ArrayList<>();

    if (filterable.isFilterable()) {
      operations.add(match(buildFilterableCriteria(filterable)));
    }
    operations.add(buildPaginationOperation(pageable));

    var aggregation = Aggregation.newAggregation(operations);
    var result =
        mongoTemplate
            .aggregate(aggregation, CandidateProfile.class, CandidateProfileFacetResult.class)
            .getUniqueMappedResult();

    return new PageImpl<>(result.getDatas(), pageable, result.getCount());
  }

  private static class CandidateProfileFacetResult extends FacetResult<SailorProfile> {
    public CandidateProfileFacetResult(
        List<SailorProfile> dataFacet, List<Map<String, Object>> countFacet) {
      super(dataFacet, countFacet);
    }
  }
}

package com.inlaco.crewmgrservice.feature.user.repository;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.limit;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.skip;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.sort;

import com.inlaco.crewmgrservice.common.model.FacetResult;
import com.inlaco.crewmgrservice.feature.user.dto.SailorFilterable;
import com.inlaco.crewmgrservice.feature.user.model.SailorProfile;
import com.inlaco.crewmgrservice.utils.PageableUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

@Slf4j
@RequiredArgsConstructor
@Repository
public class CustomSailorRepository {

  private final MongoTemplate mongoTemplate;

  public long countSailorHasCardId() {
    Query query = new Query();
    query.addCriteria(buildOfficialSailorCriteria(true));
    return mongoTemplate.count(query, SailorProfile.class);
  }

  public Page<SailorProfile> searchSailors(String keyword, Criteria filter, Pageable pageable) {
    List<Criteria> keywordCriterias = new ArrayList<>();
    if (StringUtils.hasText(keyword)) {
      String safeRegex = Pattern.quote(keyword);
      keywordCriterias.add(Criteria.where("cardId").regex(safeRegex, "i"));
      keywordCriterias.add(Criteria.where("phone").regex(safeRegex, "i"));
      keywordCriterias.add(Criteria.where("fullName").regex(safeRegex, "i"));
      keywordCriterias.add(Criteria.where("email").regex(safeRegex, "i"));
    }

    Criteria keywordCriteria =
        keywordCriterias.isEmpty()
            ? null
            : new Criteria().orOperator(keywordCriterias.toArray(Criteria[]::new));

    Criteria finalCriteria;

    if (filter != null && keywordCriteria != null) {
      finalCriteria = new Criteria().andOperator(filter, keywordCriteria);
    } else if (filter != null) {
      finalCriteria = filter;
    } else if (keywordCriteria != null) {
      finalCriteria = keywordCriteria;
    } else {
      finalCriteria = new Criteria(); // match all
    }

    pageable = PageableUtils.extendDefaultSort(pageable, SailorProfile.class);

    Aggregation aggregation =
        Aggregation.newAggregation(
            Aggregation.match(finalCriteria), buildPaginationOperation(pageable));

    SailorProfileFacetResult result =
        mongoTemplate
            .aggregate(aggregation, SailorProfile.class, SailorProfileFacetResult.class)
            .getUniqueMappedResult();

    return new PageImpl<>(result.getDatas(), pageable, result.getCount());
  }

  private Criteria buildOfficialSailorCriteria(boolean official) {
    return Criteria.where("cardId").exists(official);
  }

  private Criteria buildFilterableCriteria(SailorFilterable filterable) {
    var criteria = new Criteria();
    if (filterable == null) return criteria;

    if (filterable.getKeyword() != null) {
      criteria.andOperator(
          Criteria.where("cardId").regex(filterable.getKeyword(), "i"),
          Criteria.where("phone").regex(filterable.getKeyword(), "i"),
          Criteria.where("fullName").regex(filterable.getKeyword(), "i"),
          Criteria.where("email").regex(filterable.getKeyword(), "i"));
    }

    if (filterable.getOfficial() != null) {
      criteria.andOperator(buildOfficialSailorCriteria(filterable.getOfficial()));
    }

    if (filterable.getProfessionalPosition() != null) {
      criteria.and("professionalPosition").is(filterable.getProfessionalPosition());
    }
    if (filterable.getWorkStatus() != null) {
      criteria.and("workStatus").is(filterable.getWorkStatus());
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
    List<AggregationOperation> operations = new ArrayList<>();

    if (filterable != null && filterable.isFilterable()) {
      operations.add(match(buildFilterableCriteria(filterable)));
    }
    operations.add(buildPaginationOperation(pageable));

    var aggregation = Aggregation.newAggregation(operations);
    var result =
        mongoTemplate
            .aggregate(aggregation, SailorProfile.class, SailorProfileFacetResult.class)
            .getUniqueMappedResult();

    return new PageImpl<>(result.getDatas(), pageable, result.getCount());
  }

  private static class SailorProfileFacetResult extends FacetResult<SailorProfile> {
    public SailorProfileFacetResult(
        List<SailorProfile> dataFacet, List<Map<String, Object>> countFacet) {
      super(dataFacet, countFacet);
    }
  }
}

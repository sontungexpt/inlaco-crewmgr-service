package com.inlaco.crewmgrservice.feature.user.repository;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.limit;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.skip;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.sort;

import com.inlaco.crewmgrservice.common.model.FacetResult;
import com.inlaco.crewmgrservice.feature.user.model.CandidateProfile;
import com.inlaco.crewmgrservice.utils.PageableUtils;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

@Slf4j
@RequiredArgsConstructor
@Repository
public class CustomCandidateRepository {

  private final MongoTemplate mongoTemplate;

  public Page<CandidateProfile> searchCandidates(
      String keyword, Map<String, Object> filters, Pageable p) {
    var query = Criteria.where("fullName").regex(keyword, "i");

    if (filters != null) {
      filters.remove("fullName");
      if (!filters.isEmpty()) {
        filters.forEach((key, value) -> query.and(key).is(value));
      }
    }

    var pageable = PageableUtils.extendDefaultSort(p);
    log.debug("Fetching non expired courses with pagination");

    Aggregation aggregation =
        Aggregation.newAggregation(
            match(query),
            Aggregation.facet(Aggregation.count().as("totalCandidates"))
                .as(FacetResult.getCountFacetName())
                .and(
                    sort(pageable.getSort()),
                    skip(pageable.getOffset()),
                    limit(pageable.getPageSize()))
                .as(FacetResult.getDataFacetName()));

    var result =
        mongoTemplate
            .aggregate(aggregation, CandidateProfile.class, CandidateProfileFacetResult.class)
            .getUniqueMappedResult();

    return new PageImpl<>(result.getDatas(), pageable, result.getCount("totalCandidates"));
  }

  private class CandidateProfileFacetResult extends FacetResult<CandidateProfile> {
    public CandidateProfileFacetResult(
        List<CandidateProfile> dataFacet, List<Map<String, Object>> countFacet) {
      super(dataFacet, countFacet);
    }
  }
}

package com.inlaco.crewmgrservice.feature.user.repository;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.limit;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.skip;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.sort;

import com.inlaco.crewmgrservice.common.model.FacetResult;
import com.inlaco.crewmgrservice.feature.user.model.CandidateProfile;
import com.inlaco.crewmgrservice.feature.user.model.SailorProfile;
import com.inlaco.crewmgrservice.utils.PageableUtils;
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
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

@Slf4j
@RequiredArgsConstructor
@Repository
public class CustomSailorRepository {

  private final MongoTemplate mongoTemplate;

  public Page<SailorProfile> searchSailors(String keyword, String sailorPositionId, Pageable p) {

    Criteria query;
    if (ObjectId.isValid(keyword)) {
      query =
          new Criteria()
              .orOperator(
                  Criteria.where("accountId").is(new ObjectId(keyword)),
                  Criteria.where("fullName").regex(keyword, "i"),
                  Criteria.where("email").regex(keyword, "i"),
                  Criteria.where("phone").regex(keyword, "i"));
    } else {
      query =
          new Criteria()
              .orOperator(
                  Criteria.where("fullName").regex(keyword, "i"),
                  Criteria.where("email").regex(keyword, "i"),
                  Criteria.where("phone").regex(keyword, "i"));
    }

    if (StringUtils.hasText(sailorPositionId)) {
      query.and("sailorPositionId").is(new ObjectId(sailorPositionId));
    }

    var pageable = PageableUtils.extendDefaultSort(p);
    log.debug("Fetching non expired courses with pagination");

    Aggregation aggregation =
        Aggregation.newAggregation(
            match(query),
            Aggregation.facet(Aggregation.count().as("totalSailors"))
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

    return new PageImpl<>(result.getDatas(), pageable, result.getCount("totalSailors"));
  }

  private static class CandidateProfileFacetResult extends FacetResult<SailorProfile> {
    public CandidateProfileFacetResult(
        List<SailorProfile> dataFacet, List<Map<String, Object>> countFacet) {
      super(dataFacet, countFacet);
    }
  }
}

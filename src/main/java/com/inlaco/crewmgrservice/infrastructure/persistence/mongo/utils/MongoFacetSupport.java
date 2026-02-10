package com.inlaco.crewmgrservice.infrastructure.persistence.mongo.utils;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;

@RequiredArgsConstructor
public class MongoFacetSupport {

  private final MongoTemplate mongoTemplate;

  public record FacetResult<T>(List<T> data, long total) {}

  public <T> FacetResult<T> facet(
      Criteria match, Pageable pageable, Class<?> source, Class<T> target) {

    Aggregation aggregation =
        newAggregation(
            match(match),
            Aggregation.facet(count().as("total"))
                .as("count")
                .and(
                    sort(pageable.getSort()),
                    skip(pageable.getOffset()),
                    limit(pageable.getPageSize()))
                .as("data"));

    var raw =
        mongoTemplate.aggregate(aggregation, source, FacetRawResult.class).getUniqueMappedResult();

    if (raw == null) return new FacetResult<>(List.of(), 0);

    return new FacetResult<>(raw.data(), raw.total());
  }

  // ===== internal mapping =====
  private record FacetRawResult<T>(List<T> data, List<Count> count) {
    long total() {
      return count == null || count.isEmpty() ? 0 : count.get(0).total();
    }
  }

  private record Count(long total) {}
}

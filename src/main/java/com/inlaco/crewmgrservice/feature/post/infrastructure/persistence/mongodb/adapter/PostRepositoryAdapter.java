package com.inlaco.crewmgrservice.feature.post.infrastructure.persistence.mongodb.adapter;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

import com.inlaco.crewmgrservice.feature.post.application.model.PostSearchCriteria;
import com.inlaco.crewmgrservice.feature.post.application.port.out.PostRepository;
import com.inlaco.crewmgrservice.feature.post.domain.enums.PostType;
import com.inlaco.crewmgrservice.feature.post.domain.model.Post;
import com.inlaco.crewmgrservice.feature.post.infrastructure.persistence.mongodb.entity.PostEntity;
import com.inlaco.crewmgrservice.feature.post.infrastructure.persistence.mongodb.mapper.PostEntityMapper;
import com.inlaco.crewmgrservice.feature.post.infrastructure.persistence.mongodb.repository.PostMongoRepository;
import com.inlaco.crewmgrservice.infrastructure.persistence.mongodb.aggregation.FacetResult;
import java.time.Instant;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.jspecify.annotations.Nullable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PostRepositoryAdapter implements PostRepository {

  private final PostMongoRepository repository;
  private final PostEntityMapper mapper;
  private final MongoTemplate mongoTemplate;
  private final AuditorAware<ObjectId> auditorAware;

  @Override
  public Page<Post> findAll(Pageable pageable) {
    return repository.findByDeletedAtIsNull(pageable).map(mapper::toPost);
  }

  @Override
  public Page<Post> findAll(@Nullable PostSearchCriteria criteria, Pageable pageable) {
    var query = Criteria.where("deletedAt").exists(false);

    if (criteria != null) {
      if (criteria.getType() != null) {
        query.andOperator(Criteria.where("type").is(criteria.getType()));
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
        .aggregate(aggregation, PostEntity.class, PostEntityFacetResult.class)
        .getUniqueMappedResult()
        .toPage(pageable)
        .map(mapper::toPost);
  }

  @Override
  public Page<Post> findByType(PostType type, Pageable pageable) {
    return repository.findByTypeAndDeletedAtIsNull(type, pageable).map(mapper::toPost);
  }

  @Override
  @Cacheable(value = "posts", key = "#id", unless = "#result == null")
  public Optional<Post> findById(String id) {
    return repository.findByIdAndDeletedAtIsNull(id).map(mapper::toPost);
  }

  @Override
  @CacheEvict(value = "posts", key = "#result.id")
  public Post save(Post post) {
    String id = post.getId();
    if (id == null) {
      return mapper.toPost(repository.insert(mapper.toPostEntity(post)));
    }
    PostEntity entity =
        repository
            .findById(id)
            .map(
                existing -> {
                  mapper.updateFromPost(post, existing);
                  return existing;
                })
            .orElseGet(() -> mapper.toPostEntity(post)); // INSERT with custom id
    return mapper.toPost(repository.save(entity));
  }

  @Override
  @CacheEvict(value = "posts", key = "#id")
  public void deleteById(String id) {
    mongoTemplate.updateFirst(
        Query.query(Criteria.where("_id").is(id).and("deletedAt").exists(false)),
        new Update()
            .addToSet("deletedBy", auditorAware.getCurrentAuditor().orElse(null))
            .addToSet("deletedAt", Instant.now()),
        PostEntity.class);
  }

  static class PostEntityFacetResult extends FacetResult<PostEntity> {}
}

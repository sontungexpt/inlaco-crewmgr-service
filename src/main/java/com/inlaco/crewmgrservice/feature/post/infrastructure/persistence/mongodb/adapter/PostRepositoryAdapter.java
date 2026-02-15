package com.inlaco.crewmgrservice.feature.post.infrastructure.persistence.mongodb.adapter;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

import com.inlaco.crewmgrservice.application.exception.ResourceNotFoundException;
import com.inlaco.crewmgrservice.common.model.FacetResult;
import com.inlaco.crewmgrservice.feature.post.application.model.PostSearchCriteria;
import com.inlaco.crewmgrservice.feature.post.application.port.out.PostRepository;
import com.inlaco.crewmgrservice.feature.post.domain.enums.PostType;
import com.inlaco.crewmgrservice.feature.post.domain.model.Post;
import com.inlaco.crewmgrservice.feature.post.infrastructure.persistence.mongodb.entity.PostEntity;
import com.inlaco.crewmgrservice.feature.post.infrastructure.persistence.mongodb.mapper.PostEntityMapper;
import com.inlaco.crewmgrservice.feature.post.infrastructure.persistence.mongodb.repository.PostMongoRepository;
import java.time.Instant;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PostRepositoryAdapter implements PostRepository {

  private final PostMongoRepository postMongoRepository;
  private final PostEntityMapper mapper;
  private final MongoTemplate mongoTemplate;
  private final AuditorAware<ObjectId> auditorAware;

  @Override
  public Page<Post> findAll(Pageable pageable) {
    return postMongoRepository.findByDeletedAtIsNull(pageable).map(mapper::toPost);
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
    return postMongoRepository.findByTypeAndDeletedAtIsNull(type, pageable).map(mapper::toPost);
  }

  @Override
  public Optional<Post> findById(String id) {
    return postMongoRepository.findByIdAndDeletedAtIsNull(new ObjectId(id)).map(mapper::toPost);
  }

  @Override
  public Post save(Post post) {
    return mapper.toPost(postMongoRepository.save(mapper.toPostEntity(post)));
  }

  @Override
  public Post deleteById(String id) {
    PostEntity entity =
        postMongoRepository
            .findByIdAndDeletedAtIsNull(new ObjectId(id))
            .orElseThrow(() -> new ResourceNotFoundException(PostEntity.class, "id", id));
    entity.setDeletedAt(Instant.now());
    entity.setDeletedBy(auditorAware.getCurrentAuditor().orElse(null));
    return mapper.toPost(postMongoRepository.save(entity));
  }

  class PostEntityFacetResult extends FacetResult<PostEntity> {}
}

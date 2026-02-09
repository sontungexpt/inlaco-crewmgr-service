package com.inlaco.crewmgrservice.feature.post.infrastructure.persistence.mongodb.adapter;

import com.inlaco.crewmgrservice.application.exception.ResourceNotFoundException;
import com.inlaco.crewmgrservice.feature.post.application.port.out.PostRepository;
import com.inlaco.crewmgrservice.feature.post.domain.model.Post;
import com.inlaco.crewmgrservice.feature.post.infrastructure.persistence.mongodb.entity.PostEntity;
import com.inlaco.crewmgrservice.feature.post.infrastructure.persistence.mongodb.mapper.PostEntityMapper;
import com.inlaco.crewmgrservice.feature.post.infrastructure.persistence.mongodb.repository.PostMongoRepository;
import com.inlaco.crewmgrservice.feature.post.presentation.dto.enums.PostType;
import com.inlaco.crewmgrservice.utils.PrincipalUtils;
import java.time.Instant;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PostRepositoryAdapter implements PostRepository {

  private final PostMongoRepository postMongoRepository;
  private final PostEntityMapper mapper;

  @Override
  public Page<Post> findAll(Pageable pageable) {
    return postMongoRepository.findByDeleted(false, pageable).map(mapper::toDomain);
  }

  @Override
  public Page<Post> findByType(PostType type, Pageable pageable) {
    return postMongoRepository.findByTypeAndDeleted(type, false, pageable).map(mapper::toDomain);
  }

  @Override
  public Optional<Post> findById(String id) {
    return postMongoRepository.findByIdAndDeleted(new ObjectId(id), false).map(mapper::toDomain);
  }

  @Override
  public Post save(Post post) {
    return mapper.toDomain(postMongoRepository.save(mapper.toEntity(post)));
  }

  @Override
  public Post deleteById(String id) {
    PostEntity entity =
        postMongoRepository
            .findByIdAndDeleted(new ObjectId(id), false)
            .orElseThrow(() -> new ResourceNotFoundException(PostEntity.class, "id", id));
    entity.setDeleted(true);
    entity.setDeletedBy(new ObjectId(PrincipalUtils.getUser().getId()));
    entity.setDeletedAt(Instant.now());
    return mapper.toDomain(postMongoRepository.save(entity));
  }
}

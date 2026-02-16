package com.inlaco.crewmgrservice.feature.post.infrastructure.persistence.mongodb.repository;

import com.inlaco.crewmgrservice.feature.post.domain.enums.PostType;
import com.inlaco.crewmgrservice.feature.post.infrastructure.persistence.mongodb.entity.PostEntity;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostMongoRepository extends MongoRepository<PostEntity, String> {

  Optional<PostEntity> findByIdAndDeletedAtIsNull(String id);

  Page<PostEntity> findByDeletedAtIsNull(Pageable pageable);

  Page<PostEntity> findByTypeAndDeletedAtIsNull(PostType type, Pageable pageable);
}

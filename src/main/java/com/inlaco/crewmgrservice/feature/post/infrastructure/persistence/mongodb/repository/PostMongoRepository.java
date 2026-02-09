package com.inlaco.crewmgrservice.feature.post.infrastructure.persistence.mongodb.repository;

import com.inlaco.crewmgrservice.feature.post.infrastructure.persistence.mongodb.entity.PostEntity;
import com.inlaco.crewmgrservice.feature.post.presentation.dto.enums.PostType;
import java.util.Optional;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostMongoRepository extends MongoRepository<PostEntity, ObjectId> {

  Optional<PostEntity> findByIdAndDeleted(ObjectId id, boolean deleted);

  Page<PostEntity> findByDeleted(boolean deleted, Pageable pageable);

  Page<PostEntity> findByTypeAndDeleted(PostType type, boolean deleted, Pageable pageable);
}

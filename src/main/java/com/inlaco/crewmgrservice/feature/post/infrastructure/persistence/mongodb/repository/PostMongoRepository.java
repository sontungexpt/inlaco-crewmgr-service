package com.inlaco.crewmgrservice.feature.post.infrastructure.persistence.mongodb.repository;

import com.inlaco.crewmgrservice.feature.post.domain.enums.PostType;
import com.inlaco.crewmgrservice.feature.post.infrastructure.persistence.mongodb.entity.PostEntity;
import java.util.Optional;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostMongoRepository extends MongoRepository<PostEntity, ObjectId> {

  Optional<PostEntity> findByIdAndDeletedAtIsNull(ObjectId id);

  Page<PostEntity> findByDeletedAtIsNull(Pageable pageable);

  Page<PostEntity> findByTypeAndDeletedAtIsNull(PostType type, Pageable pageable);
}

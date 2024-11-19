package com.inlaco.crewmgrservice.feature.post.repository;

import com.inlaco.crewmgrservice.feature.post.model.Post;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends MongoRepository<Post, String> {

  Page<Post> findByAuthorId(ObjectId authorId, Pageable pageable);
}

package com.inlaco.crewmgrservice.feature.post.repository;

import com.inlaco.crewmgrservice.feature.post.model.Post;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository
    extends MongoRepository<Post, String>,
        org.springframework.data.repository.Repository<Post, String> {}

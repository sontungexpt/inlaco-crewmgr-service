package com.inlaco.crewmgrservice.feature.post.application.port.out;

import com.inlaco.crewmgrservice.feature.post.application.model.PostSearchCriteria;
import com.inlaco.crewmgrservice.feature.post.domain.enums.PostType;
import com.inlaco.crewmgrservice.feature.post.domain.model.Post;
import java.util.Optional;
import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository {

  Optional<Post> findById(String id);

  Page<Post> findAll(Pageable pageable);

  Page<Post> findAll(@Nullable PostSearchCriteria criteria, Pageable pageable);

  Page<Post> findByType(PostType type, Pageable pageable);

  Post save(Post post);

  void deleteById(String id);
}

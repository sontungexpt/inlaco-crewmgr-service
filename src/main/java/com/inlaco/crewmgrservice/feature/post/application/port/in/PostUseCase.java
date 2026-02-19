package com.inlaco.crewmgrservice.feature.post.application.port.in;

import com.inlaco.crewmgrservice.feature.post.application.model.PostSearchCriteria;
import com.inlaco.crewmgrservice.feature.post.domain.enums.PostType;
import com.inlaco.crewmgrservice.feature.post.domain.model.Post;
import com.inlaco.crewmgrservice.feature.post.domain.model.PostUpdateCommand;
import com.inlaco.crewmgrservice.feature.user.domain.model.User;
import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostUseCase {

  Post createPost(Post post, User user);

  Post updatePost(String postId, PostUpdateCommand patch, User user);

  void deletePost(String postId, User user);

  Post getPost(String postId);

  Page<Post> getPosts(@Nullable PostSearchCriteria criteria, Pageable pageable);

  @Deprecated
  Page<Post> getPagePosts(Pageable pageable, @Nullable PostType type);
}

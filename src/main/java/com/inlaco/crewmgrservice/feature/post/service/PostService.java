package com.inlaco.crewmgrservice.feature.post.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.inlaco.crewmgrservice.feature.post.enums.PostType;
import com.inlaco.crewmgrservice.feature.post.model.Post;
import com.inlaco.crewmgrservice.feature.user.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.ScrollPosition;
import org.springframework.data.domain.Window;

public interface PostService {

  Post createPost(Post post, User user);

  Post updatePost(Post post, User user);

  Post updatePost(String postId, JsonNode patch, User user);

  void deletePost(String postId, User user);

  Post getPost(String postId);

  Page<Post> getPagePosts(Pageable pageable, PostType type);

  Window<Post> getWindowPosts(ScrollPosition position);

  Page<Post> getPostsByAuthorId(String authorId, Pageable pageable);
}

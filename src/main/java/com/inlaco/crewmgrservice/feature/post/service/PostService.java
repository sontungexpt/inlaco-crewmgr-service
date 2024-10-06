package com.inlaco.crewmgrservice.feature.post.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.inlaco.crewmgrservice.feature.post.model.Post;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.ScrollPosition;
import org.springframework.data.domain.Window;

public interface PostService {

  Post createPost(Post post);

  Post updatePost(Post post);

  Post updatePost(String postId, JsonNode patch);

  void deletePost(String postId);

  Post getPost(String postId);

  Page<Post> getPagePosts(Pageable pageable);

  Window<Post> getWindowPosts(ScrollPosition position);

  List<Post> getPostsByAuthorId(String authorId);
}

package com.inlaco.crewmgrservice.feature.post.application.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.inlaco.crewmgrservice.feature.post.application.model.PostSearchCriteria;
import com.inlaco.crewmgrservice.feature.post.application.port.in.PostUseCase;
import com.inlaco.crewmgrservice.feature.post.application.port.out.PostRepository;
import com.inlaco.crewmgrservice.feature.post.domain.enums.PostType;
import com.inlaco.crewmgrservice.feature.post.domain.model.Post;
import com.inlaco.crewmgrservice.feature.user.domain.model.User;
import com.inlaco.crewmgrservice.shared.kernel.exception.ResourceNotFoundException;
import com.inlaco.crewmgrservice.shared.support.JsonMergePatchUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostService implements PostUseCase {

  private final PostRepository postRepository;
  private final JsonMergePatchUtils jsonPatchUtils;

  @Override
  public Post createPost(Post post, User user) {
    return postRepository.save(post);
  }

  @Override
  public void deletePost(String postId, User user) {
    postRepository.deleteById(postId);
  }

  @Override
  public Post getPost(String postId) {
    return postRepository
        .findById(postId)
        .orElseThrow(() -> new ResourceNotFoundException(Post.class, "id", postId));
  }

  @Override
  public Page<Post> getPagePosts(Pageable pageable, @Nullable PostType type) {
    if (type != null) return postRepository.findByType(type, pageable);
    return postRepository.findAll(pageable);
  }

  @Override
  public Post updatePost(String postId, JsonNode patch, User user) {
    Post current = getPost(postId);
    Post updated = jsonPatchUtils.apply(current, patch);
    return postRepository.save(updated);
  }

  @Override
  public Page<Post> getPosts(@Nullable PostSearchCriteria criteria, Pageable pageable) {
    return postRepository.findAll(criteria, pageable);
  }
}

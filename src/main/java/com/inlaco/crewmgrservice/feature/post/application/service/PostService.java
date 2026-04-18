package com.inlaco.crewmgrservice.feature.post.application.service;

import com.inlaco.crewmgrservice.feature.post.application.model.PostSearchCriteria;
import com.inlaco.crewmgrservice.feature.post.application.port.in.PostUseCase;
import com.inlaco.crewmgrservice.feature.post.application.port.out.PostRepository;
import com.inlaco.crewmgrservice.feature.post.domain.enums.PostType;
import com.inlaco.crewmgrservice.feature.post.domain.model.Post;
import com.inlaco.crewmgrservice.feature.post.domain.model.PostUpdateCommand;
import com.inlaco.crewmgrservice.feature.user.domain.model.User;
import com.inlaco.crewmgrservice.shared.kernel.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class PostService implements PostUseCase {

  private final PostRepository postRepository;

  @Override
  public Post createPost(Post post, User user) {
    log.info("Creating a new post by user: {}", user.getId());
    Post savedPost = postRepository.save(post);
    log.info("Post created with ID: {}", savedPost.getId());
    return savedPost;
  }

  @Override
  public void deletePost(String postId, User user) {
    log.info("Deleting post with ID: {} by user: {}", postId, user.getId());
    postRepository.deleteById(postId);
    log.info("Post with ID: {} deleted successfully", postId);
  }

  @Override
  public Post getPost(String postId) {
    log.debug("Fetching post with ID: {}", postId);
    return postRepository
        .findById(postId)
        .orElseThrow(
            () -> {
              log.warn("Post not found with ID: {}", postId);
              return new ResourceNotFoundException(Post.class, "id", postId);
            });
  }

  @Override
  public Page<Post> getPagePosts(Pageable pageable, @Nullable PostType type) {
    if (type != null) {
      log.debug("Fetching posts of type: {}", type);
      return postRepository.findByType(type, pageable);
    }
    log.debug("Fetching all posts");
    return postRepository.findAll(pageable);
  }

  @Override
  public Post updatePost(String postId, PostUpdateCommand patch, User user) {
    log.info("Updating post with ID: {} by user: {}", postId, user.getId());
    Post current = getPost(postId);
    current.update(patch);
    Post updatedPost = postRepository.save(current);
    log.info("Post with ID: {} updated successfully", postId);
    return updatedPost;
  }

  @Override
  public Page<Post> getPosts(@Nullable PostSearchCriteria criteria, Pageable pageable) {
    log.debug("Fetching posts with criteria: {}", criteria);
    return postRepository.findAll(criteria, pageable);
  }
}

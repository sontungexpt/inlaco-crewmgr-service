package com.inlaco.crewmgrservice.feature.post.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.inlaco.crewmgrservice.exceptions.ResourceNotFoundException;
import com.inlaco.crewmgrservice.feature.post.model.Post;
import com.inlaco.crewmgrservice.feature.post.repository.PostRepository;
import com.inlaco.crewmgrservice.feature.post.service.PostService;
import com.inlaco.crewmgrservice.utils.JsonPatchUtils;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.ScrollPosition;
import org.springframework.data.domain.Window;
import org.springframework.stereotype.Service;

@Service
public record PostServiceImpl(PostRepository postRepository, JsonPatchUtils jsonPatchUtils)
    implements PostService {

  @Override
  public Post createPost(Post post) {
    return postRepository.save(post);
  }

  @Override
  public void deletePost(String postId) {
    postRepository.deleteById(postId);
  }

  @Override
  public Post getPost(String postId) {
    return postRepository
        .findById(postId)
        .orElseThrow(() -> new ResourceNotFoundException(Post.class, "id", postId));
  }

  @Override
  public List<Post> getPostsByAuthorId(String authorId) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getPostsByAuthorId'");
  }

  @Override
  public Page<Post> getPagePosts(Pageable pageable) {
    return postRepository.findAll(pageable);
  }

  @Override
  public Window<Post> getWindowPosts(ScrollPosition position) {

    return null;
  }

  @Override
  public Post updatePost(Post post) {
    return postRepository.save(post);
  }

  @Override
  public Post updatePost(String postId, JsonNode patch) {
    Post oldPost = getPost(postId);
    return postRepository.save(jsonPatchUtils.applyMergePatch(oldPost, patch));
  }
}

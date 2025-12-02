package com.inlaco.crewmgrservice.feature.post.service.impl;

import com.inlaco.crewmgrservice.exceptions.ResourceNotFoundException;
import com.inlaco.crewmgrservice.feature.post.model.Post;
import com.inlaco.crewmgrservice.feature.post.model.RecruitmentPost;
import com.inlaco.crewmgrservice.feature.post.repository.PostRepository;
import com.inlaco.crewmgrservice.feature.post.service.RecruitmentPostService;
import com.inlaco.crewmgrservice.feature.user.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RecruitmentPostServiceImpl implements RecruitmentPostService {

  private final PostRepository postRepository;

  @Override
  public void changeRegistrationStatus(String postId, boolean active, User user) {
    Post post =
        postRepository
            .findById(postId)
            .orElseThrow(() -> new ResourceNotFoundException(Post.class, "id", postId));

    if (post instanceof RecruitmentPost) {
      ((RecruitmentPost) post).setCanceled(!active);
      postRepository.save(post);
      return;
    }

    throw new ResourceNotFoundException(Post.class, "id", postId);
  }
}

package com.inlaco.crewmgrservice.feature.post.application.service;

import com.inlaco.crewmgrservice.feature.post.application.port.in.RecruitmentPostUseCase;
import com.inlaco.crewmgrservice.feature.post.application.port.out.PostRepository;
import com.inlaco.crewmgrservice.feature.post.domain.model.Post;
import com.inlaco.crewmgrservice.feature.post.domain.model.RecruitmentPost;
import com.inlaco.crewmgrservice.feature.user.domain.model.User;
import com.inlaco.crewmgrservice.shared.kernel.exception.ResourceNotFoundException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RecruitmentPostService implements RecruitmentPostUseCase {

  private final PostRepository postRepository;

  @Override
  public void changeRegistrationStatus(
      String postId, boolean active, Instant reopenUntil, User user) {
    Post post =
        postRepository
            .findById(postId)
            .orElseThrow(() -> new ResourceNotFoundException(Post.class, "id", postId));
    if (!(post instanceof RecruitmentPost recruitmentPost)) {
      throw new ResourceNotFoundException(Post.class, "id", postId);
    }

    if (active) {
      if (reopenUntil == null) {
        // plus 10 days from now
        reopenUntil = Instant.now().plus(10, ChronoUnit.DAYS);
      }
      recruitmentPost.forceOpenUntil(reopenUntil);
    } else {
      recruitmentPost.cancel();
    }

    postRepository.save(post);
  }
}

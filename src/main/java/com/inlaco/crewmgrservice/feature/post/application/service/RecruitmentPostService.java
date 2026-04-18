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
  private static final int DEFAULT_OPEN_UNTIL_DAYS = 10;

  @Override
  public void changeRegistrationStatus(
      String postId, boolean active, Instant reopenUntil, User user) {
    log.debug("Changing registration status for post ID: {}", postId);
    Post post =
        postRepository
            .findById(postId)
            .orElseThrow(
                () -> {
                  log.warn("Post not found with ID: {}", postId);
                  return new ResourceNotFoundException(Post.class, "id", postId);
                });
    if (!(post instanceof RecruitmentPost recruitmentPost)) {
      log.warn("Post with ID: {} is not a RecruitmentPost", postId);
      throw new ResourceNotFoundException(Post.class, "id", postId);
    }

    if (active) {
      if (reopenUntil == null) {
        log.debug("Reopen until date not provided, defaulting to 10 days from now");
        reopenUntil = Instant.now().plus(DEFAULT_OPEN_UNTIL_DAYS, ChronoUnit.DAYS);
      }
      recruitmentPost.forceOpenUntil(reopenUntil);
      log.info("Post ID: {} registration reopened until {}", postId, reopenUntil);
    } else {
      recruitmentPost.cancel();
      log.info("Post ID: {} registration canceled", postId);
    }

    postRepository.save(post);
    log.info("Post ID: {} registration status updated successfully", postId);
  }
}

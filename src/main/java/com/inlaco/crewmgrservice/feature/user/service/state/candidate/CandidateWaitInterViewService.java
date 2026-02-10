package com.inlaco.crewmgrservice.feature.user.service.state.candidate;

import com.inlaco.crewmgrservice.feature.notify.NotificationDispatcher;
import com.inlaco.crewmgrservice.feature.notify.NotificationPolicy;
import com.inlaco.crewmgrservice.feature.notify.mail.EmailRequest;
import com.inlaco.crewmgrservice.feature.post.application.port.in.PostUseCase;
import com.inlaco.crewmgrservice.feature.post.domain.model.RecruitmentPost;
import com.inlaco.crewmgrservice.feature.user.model.CandidateProfile;
import com.inlaco.crewmgrservice.feature.user.model.CandidateProfile.Status;
import com.inlaco.crewmgrservice.feature.user.model.User;
import com.inlaco.crewmgrservice.feature.user.repository.CandidateProfileRepository;
import com.inlaco.crewmgrservice.feature.user.service.UserService;
import com.inlaco.crewmgrservice.utils.TextTemplateBuilder;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service(ReviewService.WAIT_INTERVIEW)
public class CandidateWaitInterViewService extends CandidateReviewStragegy {

  private String EMAIL_SUBJECT = "Inlaco - Your profile is review";

  public CandidateWaitInterViewService(
      CandidateProfileRepository candidateProfileRepository,
      NotificationDispatcher notificationFactory,
      PostUseCase postService,
      UserService userService) {
    super(candidateProfileRepository, notificationFactory, postService, userService);
  }

  @Override
  @Transactional
  public void updateProfileStatus(CandidateProfile profile) {
    profile.setStatus(Status.WAIT_FOR_INTERVIEW);
    candidateProfileRepository.save(profile);
    User user = userService.findUserById(profile.getAccountId().toHexString());
    user.promoteJobState();
    userService.saveUser(user);
  }

  @Override
  @Transactional
  public void review(CandidateProfile profile, boolean autoEmail) {
    if (profile.getStatus() == Status.WAIT_FOR_INTERVIEW) return;
    log.info("Review candidate: {}", profile.getFullName());
    if (autoEmail) {
      RecruitmentPost post =
          (RecruitmentPost) postService.getPost(profile.getRecruitmentPostId().toHexString());

      try {
        notificationFactory.sendNotificationAsync(
            NotificationPolicy.EMAIL,
            EmailRequest.html(
                    profile.getEmail(),
                    TextTemplateBuilder.relativePath(
                            "src/main/resources/templates/email/html/recruitment/wait-for-interview.html")
                        .var("candidate_name", profile.getFullName())
                        .var("position_name", post.getPosition())
                        .buildContent(),
                    EMAIL_SUBJECT)
                .build());
      } catch (IOException e) {
        e.printStackTrace();
      }

      log.info("Send email to candidate: {}", profile.getEmail());
    }

    updateProfileStatus(profile);
  }
}

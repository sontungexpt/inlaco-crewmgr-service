package com.inlaco.crewmgrservice.feature.user.service.state.candidate;

import com.inlaco.crewmgrservice.feature.notify.NotificationFactory;
import com.inlaco.crewmgrservice.feature.notify.NotificationType;
import com.inlaco.crewmgrservice.feature.notify.mail.EmailRequest;
import com.inlaco.crewmgrservice.feature.post.model.RecruitmentPost;
import com.inlaco.crewmgrservice.feature.post.service.PostService;
import com.inlaco.crewmgrservice.feature.user.model.CandidateProfile;
import com.inlaco.crewmgrservice.feature.user.model.CandidateProfile.Status;
import com.inlaco.crewmgrservice.feature.user.model.User;
import com.inlaco.crewmgrservice.feature.user.repository.CandidateProfileRepository;
import com.inlaco.crewmgrservice.feature.user.service.UserService;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.mapping.Unwrapped.Nullable;
import org.springframework.stereotype.Service;

@Slf4j
@Service(ReviewService.HIRED)
public class CandidateHiredService extends CandidateReviewStragegy {

  private String EMAIL_SUBJECT = "Inlaco - You are hired!";

  public CandidateHiredService(
      CandidateProfileRepository candidateProfileRepository,
      NotificationFactory notificationFactory,
      PostService postService,
      UserService userService) {
    super(candidateProfileRepository, notificationFactory, postService, userService);
  }

  @Override
  public void updateProfileStatus(CandidateProfile profile) {
    profile.setStatus(Status.HIRED);
    candidateProfileRepository.save(profile);
    User user = userService.findUserById(profile.getAccountId().toHexString());
    user.promoteJobState();
    userService.saveUser(user);
  }

  @Nullable
  private String createHtmlEmail(String candidate_name, String position) {
    try {
      String html =
          Files.readString(
              Paths.get("src/main/resources/templates/email/html/recruitment/hired.html"));

      return html.replace("${candidate_name}", candidate_name)
          .replace("${position_name}", position);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  @Override
  public void review(String candidateId, boolean autoEmail) {
    CandidateProfile profile = getCandidateProfile(candidateId);
    log.info("Hire candidate: {}", profile.getFullName());
    if (autoEmail) {
      RecruitmentPost post =
          (RecruitmentPost) postService.getPost(profile.getRecruimentPostId().toHexString());

      String email = createHtmlEmail(profile.getFullName(), post.getPosition());

      EmailRequest emailRequest = new EmailRequest(profile.getEmail(), email, EMAIL_SUBJECT);

      notificationFactory.sendNotificationAsync(NotificationType.EMAIL, emailRequest);
      log.info("Send email to candidate: {}", profile.getFullName());
    }
    updateProfileStatus(profile);
  }
}

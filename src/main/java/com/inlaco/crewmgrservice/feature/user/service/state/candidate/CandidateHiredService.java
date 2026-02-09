package com.inlaco.crewmgrservice.feature.user.service.state.candidate;

import com.inlaco.crewmgrservice.feature.notify.NotificationFactory;
import com.inlaco.crewmgrservice.feature.notify.NotificationType;
import com.inlaco.crewmgrservice.feature.notify.mail.EmailRequest;
import com.inlaco.crewmgrservice.feature.post.application.port.in.PostUseCase;
import com.inlaco.crewmgrservice.feature.post.domain.model.RecruitmentPost;
import com.inlaco.crewmgrservice.feature.user.model.CandidateProfile;
import com.inlaco.crewmgrservice.feature.user.model.CandidateProfile.Status;
import com.inlaco.crewmgrservice.feature.user.model.User;
import com.inlaco.crewmgrservice.feature.user.repository.CandidateProfileRepository;
import com.inlaco.crewmgrservice.feature.user.service.UserService;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.mongodb.core.mapping.Unwrapped.Nullable;
import org.springframework.stereotype.Service;

@Slf4j
@Service(ReviewService.HIRED)
public class CandidateHiredService extends CandidateReviewStragegy {

  @Value("${inlaco.template.email.hired.subject:Inlaco - You are hired!}")
  private String EMAIL_SUBJECT;

  @Value("${inlaco.template.email.hired.path}")
  private String TEMPLATE_PATH;

  public CandidateHiredService(
      CandidateProfileRepository candidateProfileRepository,
      NotificationFactory notificationFactory,
      PostUseCase postService,
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

  @Override
  public void review(CandidateProfile profile, boolean autoEmail) {
    if (profile.getStatus() == Status.HIRED) return;
    log.debug("Hire candidate: {}", profile.getFullName());
    if (autoEmail) {
      RecruitmentPost post =
          (RecruitmentPost) postService.getPost(profile.getRecruitmentPostId().toHexString());

      String email = createHtmlEmail(profile.getFullName(), post.getPosition());

      EmailRequest emailRequest =
          EmailRequest.html(profile.getEmail(), email, EMAIL_SUBJECT).build();

      notificationFactory.sendNotificationAsync(NotificationType.EMAIL, emailRequest);

      log.debug("Send email to candidate: {}", profile.getFullName());
    }
    updateProfileStatus(profile);
  }

  @Nullable
  private String createHtmlEmail(String candidate_name, String position) {
    try {
      // String html =
      //     Files.readString(
      //         Paths.get("src/main/resources/templates/email/html/recruitment/hired.html"));

      ClassPathResource resource = new ClassPathResource(TEMPLATE_PATH);
      String html = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);

      return html.replace("${candidate_name}", candidate_name)
          .replace("${position_name}", position);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }
}

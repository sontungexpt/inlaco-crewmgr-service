package com.inlaco.crewmgrservice.feature.recruitment.infrastructure.listener;

import com.inlaco.crewmgrservice.feature.notify.NotificationDispatcher;
import com.inlaco.crewmgrservice.feature.notify.NotificationPolicy;
import com.inlaco.crewmgrservice.feature.notify.mail.EmailRequest;
import com.inlaco.crewmgrservice.feature.post.application.port.in.PostUseCase;
import com.inlaco.crewmgrservice.feature.post.domain.model.RecruitmentPost;
import com.inlaco.crewmgrservice.feature.recruitment.domain.enums.ApplicationStatus;
import com.inlaco.crewmgrservice.feature.recruitment.domain.event.ApplicationStatusChangedEvent;
import com.inlaco.crewmgrservice.feature.recruitment.domain.model.JobApplication;
import com.inlaco.crewmgrservice.feature.recruitment.infrastructure.config.RecruitmentEmailProperties;
import com.inlaco.crewmgrservice.shared.template.TextTemplateBuilder;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ApplicationStatusChangedEventListener {

  private final PostUseCase postUseCase;
  private final RecruitmentEmailProperties emailProperties;
  private final NotificationDispatcher notificationDispatcher;
  private final Map<ApplicationStatus, String> templateCache = new ConcurrentHashMap<>();

  @EventListener(ApplicationStatusChangedEvent.class)
  public void handleApplicationReviewed(ApplicationStatusChangedEvent event) {
    JobApplication application = event.application();

    if (!emailProperties.getTemplates().containsKey(application.getStatus())) {
      return; // status không cần gửi mail
    }

    sendEmail(application);
  }

  private void sendEmail(JobApplication application) {

    RecruitmentPost post =
        (RecruitmentPost) postUseCase.getPost(application.getRecruitmentPostId());

    ApplicationStatus status = application.getStatus();

    String rawTemplate = loadTemplate(status);

    String content =
        TextTemplateBuilder.content(rawTemplate)
            .var("candidate_name", application.getFullName())
            .var("position_name", post.getPosition())
            .buildContent();

    String subject = getEmailSubject(status);

    EmailRequest request = EmailRequest.html(application.getEmail(), content, subject).build();

    notificationDispatcher.sendNotificationAsync(NotificationPolicy.EMAIL, request);
  }

  private String getEmailSubject(ApplicationStatus status) {
    return emailProperties.getTemplates().get(status).subject();
  }

  private String loadTemplate(ApplicationStatus status) {
    return templateCache.computeIfAbsent(status, this::fetchTemplate);
  }

  private String fetchTemplate(ApplicationStatus status) {
    try {
      String path = emailProperties.getTemplates().get(status).path();
      ClassPathResource resource = new ClassPathResource(path);
      return new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
    } catch (IOException e) {
      throw new IllegalStateException("Cannot load template for status: " + status, e);
    }
  }
}

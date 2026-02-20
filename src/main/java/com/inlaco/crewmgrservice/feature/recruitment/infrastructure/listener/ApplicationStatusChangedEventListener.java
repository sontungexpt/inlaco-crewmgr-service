package com.inlaco.crewmgrservice.feature.recruitment.infrastructure.listener;

import com.inlaco.crewmgrservice.feature.notify.NotificationDispatcher;
import com.inlaco.crewmgrservice.feature.notify.NotificationPolicy;
import com.inlaco.crewmgrservice.feature.notify.mail.EmailRequest;
import com.inlaco.crewmgrservice.feature.recruitment.domain.enums.ApplicationStatus;
import com.inlaco.crewmgrservice.feature.recruitment.domain.event.ApplicationStatusChangedEvent;
import com.inlaco.crewmgrservice.feature.recruitment.domain.model.JobApplication;
import com.inlaco.crewmgrservice.feature.recruitment.infrastructure.config.RecruitmentEmailProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Slf4j
@Component
@RequiredArgsConstructor
public class ApplicationStatusChangedEventListener {

  private final RecruitmentEmailProperties emailProperties;
  private final NotificationDispatcher notificationDispatcher;
  private final SpringTemplateEngine templateEngine;

  @EventListener
  public void handleApplicationReviewed(ApplicationStatusChangedEvent event) {
    JobApplication application = event.application();

    if (!emailProperties.getTemplates().containsKey(application.getStatus())) {
      log.info("Status {} is not emailable", application.getStatus());
      return; // status is not emailable
    }

    sendEmail(application);
  }

  private void sendEmail(JobApplication application) {
    ApplicationStatus status = application.getStatus();
    notificationDispatcher.sendNotificationAsync(
        NotificationPolicy.EMAIL,
        EmailRequest.html(
                application.getEmail(),
                buildBodyContent(emailProperties.getTemplates().get(status).path(), application),
                getEmailSubject(status))
            .build());
  }

  private String getEmailSubject(ApplicationStatus status) {
    return emailProperties.getTemplates().get(status).subject();
  }

  private final String buildBodyContent(String path, JobApplication application) {
    var context = new Context();
    context.setVariable("candidate_name", application.getFullName());
    context.setVariable("position_name", application.getPosition());
    // context.setVariable("company_name", "INLACO");
    // context.setVariable("current_year", String.format("%d", Year.now().getValue()));
    // context.setVariable("contact_email", "inlaco@gmail.com");
    return templateEngine.process(path, context);
  }
}

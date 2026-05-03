package com.inlaco.crewmgrservice.feature.recruitment.infrastructure.listener;

import com.inlaco.crewmgrservice.feature.notify.sender.NotificationDispatcher;
import com.inlaco.crewmgrservice.feature.notify.sender.email.EmailRequest;
import com.inlaco.crewmgrservice.feature.recruitment.application.event.JobApplicationSubmittedEvent;
import com.inlaco.crewmgrservice.feature.recruitment.domain.enums.ApplicationStatus;
import com.inlaco.crewmgrservice.feature.recruitment.domain.model.JobApplication;
import com.inlaco.crewmgrservice.feature.recruitment.infrastructure.config.RecruitmentEmailProperties;
import java.time.Year;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Slf4j
@Component
@RequiredArgsConstructor
public class ApplicationSubmittedEventListener {

  private final NotificationDispatcher notificationDispatcher;
  private final SpringTemplateEngine templateEngine;
  private final RecruitmentEmailProperties emailProperties;

  @EventListener
  public void handleApplicationSubmitted(JobApplicationSubmittedEvent event) {
    JobApplication application = event.application();

    log.info(
        "Received ApplicationSubmittedEvent for candidate='{}', email='{}', position='{}'",
        application.getFullName(),
        application.getEmail(),
        application.getPosition());

    sendEmail(application);

    log.debug(
        "Completed handling ApplicationSubmittedEvent for candidate='{}'",
        application.getFullName());
  }

  private void sendEmail(JobApplication application) {
    var template = emailProperties.getTemplate(ApplicationStatus.APPLIED);

    if (template == null) {
      log.warn(
          "No email template configured for status={} applicationId={}",
          ApplicationStatus.APPLIED,
          application.getId());
      return;
    }

    log.debug(
        "Preparing email for applicationId={} using template={}",
        application.getId(),
        template.path());

    String subject = template.subject();
    String body = buildEmailBody(template.path(), application);

    log.info(
        "Dispatching application submitted email to '{}' for candidate='{}'",
        application.getEmail(),
        application.getFullName());

    notificationDispatcher.sendNotificationAsync(
        EmailRequest.html(application.getEmail(), body, subject).build());

    log.debug(
        "Notification dispatched for applicationId={} to={}",
        application.getId(),
        application.getEmail());
  }

  private String buildEmailBody(String path, JobApplication application) {
    var context = new Context();
    context.setVariable("candidate_name", application.getFullName());
    context.setVariable("position_name", application.getPosition());
    context.setVariable("company_name", "INLACO");
    context.setVariable("current_year", Year.now().getValue());
    context.setVariable("contact_email", "inlaco@gmail.com");

    log.debug(
        "Building email body using template='{}' for candidate='{}'",
        path,
        application.getFullName());

    return templateEngine.process(path, context);
  }
}

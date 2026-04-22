package com.inlaco.crewmgrservice.feature.recruitment.infrastructure.listener;

import com.inlaco.crewmgrservice.feature.notify.application.port.service.NotificationDispatcher;
import com.inlaco.crewmgrservice.feature.notify.domain.model.EmailRequest;
import com.inlaco.crewmgrservice.feature.recruitment.domain.enums.ApplicationStatus;
import com.inlaco.crewmgrservice.feature.recruitment.domain.event.ApplicationStatusChangedEvent;
import com.inlaco.crewmgrservice.feature.recruitment.domain.model.JobApplication;
import com.inlaco.crewmgrservice.feature.recruitment.infrastructure.config.RecruitmentEmailProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Slf4j
@Component
@RequiredArgsConstructor
public class ApplicationStatusChangedEventListener {

  private final RecruitmentEmailProperties emailProperties;
  private final NotificationDispatcher notificationDispatcher;
  private final SpringTemplateEngine templateEngine;

  @TransactionalEventListener
  public void handleApplicationReviewed(ApplicationStatusChangedEvent event) {
    JobApplication application = event.application();

    // Detailed debug to help trace event handling without being noisy in production.
    log.debug(
        "Received ApplicationStatusChangedEvent for applicationId={} status={}",
        application.getId(),
        application.getStatus());

    if (!emailProperties.getTemplates().containsKey(application.getStatus())) {
      // This is an expected/normal branch for many status changes, reduce to debug.
      log.debug(
          "Status {} is not emailable for applicationId={}",
          application.getStatus(),
          application.getId());
      return; // status is not emailable
    }

    // Info-level: we are about to send an email for a status change (meaningful action).
    log.info(
        "Sending status update email for applicationId={} to={} status={}",
        application.getId(),
        application.getEmail(),
        application.getStatus());

    sendEmail(application);
  }

  private void sendEmail(JobApplication application) {
    ApplicationStatus status = application.getStatus();

    // Useful internals at debug level
    log.debug(
        "Building email content for applicationId={} status={} template={}",
        application.getId(),
        status,
        emailProperties.getTemplates().get(status).path());

    notificationDispatcher.sendNotificationAsync(
        EmailRequest.html(
                application.getEmail(),
                buildBodyContent(emailProperties.getTemplates().get(status).path(), application),
                getEmailSubject(status))
            .build());

    // Indicate that the dispatch call was made; the actual delivery is asynchronous.
    log.debug(
        "Dispatched email notification for applicationId={} to={}",
        application.getId(),
        application.getEmail());
  }

  private String getEmailSubject(ApplicationStatus status) {
    return emailProperties.getTemplates().get(status).subject();
  }

  private final String buildBodyContent(String path, JobApplication application) {
    var context = new Context();
    context.setVariable("candidate_name", application.getFullName());
    context.setVariable("position_name", application.getPosition());
    return templateEngine.process(path, context);
  }
}

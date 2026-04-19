package com.inlaco.crewmgrservice.feature.recruitment.infrastructure.listener;

import com.inlaco.crewmgrservice.feature.notify.NotificationDispatcher;
import com.inlaco.crewmgrservice.feature.notify.NotificationPolicy;
import com.inlaco.crewmgrservice.feature.notify.mail.EmailRequest;
import com.inlaco.crewmgrservice.feature.recruitment.application.event.ApplicationSubmittedEvent;
import com.inlaco.crewmgrservice.feature.recruitment.domain.model.JobApplication;
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

  @EventListener
  public void handleApplicationReviewed(ApplicationSubmittedEvent event) {
    JobApplication application = event.application();

    // High-level info for observability: who triggered the event
    log.info(
        "Received ApplicationSubmittedEvent for candidate='{}', email='{}', position='{}'",
        application.getFullName(),
        application.getEmail(),
        application.getPosition());

    sendEmail(application);

    // Traceable confirmation that processing of the event completed
    log.debug(
        "Completed handling ApplicationSubmittedEvent for candidate='{}'",
        application.getFullName());
  }

  private void sendEmail(JobApplication application) {
    final String COMPANY_NAME = "INLACO";

    // Helpful debug-level detail before building the email body
    log.debug(
        "Preparing Application Submitted email for candidate='{}' (email='{}')",
        application.getFullName(),
        application.getEmail());

    String subject = "Application Successful - " + COMPANY_NAME;
    String body = buildEmailBody(application);

    // Info-level log to indicate an outbound notification is being dispatched
    log.info(
        "Dispatching application submitted email to '{}' for candidate='{}'",
        application.getEmail(),
        application.getFullName());

    notificationDispatcher.sendNotificationAsync(
        NotificationPolicy.EMAIL, EmailRequest.html(application.getEmail(), body, subject).build());

    // Debug-level log to indicate the async dispatch call has been made
    log.debug(
        "NotificationDispatcher.sendNotificationAsync called for candidate='{}', email='{}'",
        application.getFullName(),
        application.getEmail());
  }

  private String buildEmailBody(JobApplication application) {
    var context = new Context();
    context.setVariable("candidate_name", application.getFullName());
    context.setVariable("position_name", application.getPosition());
    context.setVariable("company_name", "INLACO");
    context.setVariable("current_year", String.format("%d", Year.now().getValue()));
    context.setVariable("contact_email", "inlaco@gmail.com");

    // Debug to trace template processing input (template name is constant here)
    log.debug(
        "Building email body using template 'mail/recruitment/applied.html' for candidate='{}'",
        application.getFullName());

    return templateEngine.process("mail/recruitment/applied.html", context);
  }
}

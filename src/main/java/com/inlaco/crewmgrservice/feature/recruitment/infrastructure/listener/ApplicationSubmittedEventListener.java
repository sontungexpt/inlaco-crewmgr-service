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
    sendEmail(application);
  }

  private void sendEmail(JobApplication application) {
    final String COMPANY_NAME = "INLACO";
    notificationDispatcher.sendNotificationAsync(
        NotificationPolicy.EMAIL,
        EmailRequest.html(
                application.getEmail(),
                buildEmailBody(application),
                "Application Successful - " + COMPANY_NAME)
            .build());
  }

  private String buildEmailBody(JobApplication application) {
    var context = new Context();
    context.setVariable("candidate_name", application.getFullName());
    context.setVariable("position_name", application.getPosition());
    context.setVariable("company_name", "INLACO");
    context.setVariable("current_year", String.format("%d", Year.now().getValue()));
    context.setVariable("contact_email", "inlaco@gmail.com");
    return templateEngine.process("mail/recruitment/applied.html", context);
  }
}

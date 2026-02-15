package com.inlaco.crewmgrservice.feature.recruitment.infrastructure.listener;

import com.inlaco.crewmgrservice.feature.notify.NotificationDispatcher;
import com.inlaco.crewmgrservice.feature.notify.NotificationPolicy;
import com.inlaco.crewmgrservice.feature.notify.mail.EmailRequest;
import com.inlaco.crewmgrservice.feature.recruitment.application.event.ApplicationSubmittedEvent;
import com.inlaco.crewmgrservice.feature.recruitment.domain.model.JobApplication;
import com.inlaco.crewmgrservice.shared.template.TextTemplateBuilder;
import java.io.IOException;
import java.time.Year;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ApplicationSubmittedEventListener {

  private final NotificationDispatcher notificationDispatcher;

  @EventListener(ApplicationSubmittedEvent.class)
  public void handleApplicationReviewed(ApplicationSubmittedEvent event) {
    JobApplication application = event.application();
    sendEmail(application);
  }

  private String sendEmail(JobApplication application) {
    final String COMPANY_NAME = "INLACO";
    try {
      notificationDispatcher.sendNotificationAsync(
          NotificationPolicy.EMAIL,
          EmailRequest.html(
                  application.getEmail(),
                  TextTemplateBuilder.relativePath(
                          "src/main/resources/templates/email/html/recruitment/applied.html")
                      .var("candidate_name", application.getFullName())
                      .var("position_name", application.getPosition())
                      .var("company_name", COMPANY_NAME)
                      .var("current_year", String.format("%d", Year.now().getValue()))
                      .var("contact_email", "inlaco@gmail.com")
                      .buildContent(),
                  "Application Successful - " + COMPANY_NAME)
              .build());

    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }
}

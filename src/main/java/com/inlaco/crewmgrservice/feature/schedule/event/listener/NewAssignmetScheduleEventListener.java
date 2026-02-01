package com.inlaco.crewmgrservice.feature.schedule.event.listener;

import com.inlaco.crewmgrservice.feature.notify.NotificationFactory;
import com.inlaco.crewmgrservice.feature.notify.NotificationType;
import com.inlaco.crewmgrservice.feature.notify.mail.EmailRequest;
import com.inlaco.crewmgrservice.feature.schedule.event.NewAssignmentScheduleEvent;
import com.inlaco.crewmgrservice.feature.schedule.model.AssignedMobilization;
import com.inlaco.crewmgrservice.feature.user.model.SailorProfile;
import com.inlaco.crewmgrservice.feature.user.service.SailorService;
import com.inlaco.crewmgrservice.utils.TextTemplateBuilder;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class NewAssignmetScheduleEventListener {
  private final SailorService sailorService;
  private final NotificationFactory notificationFactory;

  @Value("${inlaco.client.base-url}")
  private String CLIENT_HOME_PAGE_LINK;

  @Value("${inlaco.template.email.sailor-schedule.path}")
  private String EMAIL_TEMPLATE_PATH;

  @Value("${inlaco.template.email.sailor-schedule.subject}")
  private String EMAIL_SUBJECT;

  private volatile String cachedTemplate;

  @EventListener(NewAssignmentScheduleEvent.class)
  public void handleNewAssignmentScheduleEvent(NewAssignmentScheduleEvent event) {
    var schedule = event.getAssigmentSchedule();
    log.info("Handling schedule notification [id={}]", schedule.getId());
    notifySailorSchedule(schedule);
  }

  private void notifySailorSchedule(AssignedMobilization schedule) {
    if (schedule.getCrewMembers() == null || schedule.getCrewMembers().isEmpty()) {
      log.warn("Schedule {} has no crew members to notify", schedule.getId());
      return;
    }
    List<String> cardIds = schedule.getCrewMembers().stream().map(it -> it.getCardId()).toList();
    List<SailorProfile> profiles = sailorService.findSailorProfilesByCardIds(cardIds);

    if (profiles.isEmpty()) {
      log.warn("No sailor profiles found for schedule {}", schedule.getId());
      return;
    }

    String emailTemplate = getEmailTemplate();
    if (emailTemplate == null) {
      return;
    }
    profiles.forEach(profile -> sendScheduleEmail(profile, schedule, emailTemplate));
  }

  private void sendScheduleEmail(
      SailorProfile profile, AssignedMobilization schedule, String template) {
    if (profile.getEmail() == null || profile.getEmail().isBlank()) {
      log.warn("Skip notifying sailor {} due to missing email", profile.getId());
      return;
    }

    EmailRequest emailRequest =
        EmailRequest.html(
                profile.getEmail(),
                TextTemplateBuilder.content(template)
                    .var("recipient_name", profile.getFullName())
                    .var("company_name", "Inlaco")
                    .var("start_date", schedule.getStartDate().toString())
                    .var("estimated_end_date", schedule.getEndDate().toString())
                    .var("home_page_link", CLIENT_HOME_PAGE_LINK)
                    .var("info_link", "")
                    .buildContent(),
                EMAIL_SUBJECT)
            .build();

    log.debug(
        "Sending schedule notification email to sailor [id={}, email={}]",
        profile.getId(),
        profile.getEmail());

    notificationFactory.sendNotificationAsync(NotificationType.EMAIL, emailRequest);
  }

  /** Lazy-load the HTML template */
  private String getEmailTemplate() {
    if (cachedTemplate == null) {
      synchronized (this) {
        if (cachedTemplate == null) {
          try {
            ClassPathResource resource = new ClassPathResource(EMAIL_TEMPLATE_PATH);
            cachedTemplate =
                new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
          } catch (IOException e) {
            log.error("Failed to load email template with path {}", EMAIL_TEMPLATE_PATH, e);
            throw new RuntimeException("Failed to generate email request");
          }
        }
      }
    }
    return cachedTemplate;
  }
}

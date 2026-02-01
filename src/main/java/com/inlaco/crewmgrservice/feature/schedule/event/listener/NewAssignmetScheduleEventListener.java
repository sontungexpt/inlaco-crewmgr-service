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
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class NewAssignmetScheduleEventListener {
  @Value("${inlaco.client.base-url}")
  private String CLIENT_HOME_PAGE_LINK;

  private String SAILOR_WORK_EMAIL_NOTIFICATION_PATH =
      "src/main/resources/templates/email/html/schedule/sailor-work-notification.html";

  private final SailorService sailorService;
  private final NotificationFactory notificationFactory;

  @EventListener(NewAssignmentScheduleEvent.class)
  public void handleNewAssignmentScheduleEvent(NewAssignmentScheduleEvent event) {
    var schedule = event.getAssigmentSchedule();
    log.info("Handling schedule notification [id={}]", schedule.getId());
    notifySailorSchedule(schedule);
  }

  private String loadEmailTemplate() {
    try {
      return Files.readString(Paths.get(SAILOR_WORK_EMAIL_NOTIFICATION_PATH));
    } catch (IOException e) {
      log.error(
          "Failed to load sailor work notification email template from {}",
          SAILOR_WORK_EMAIL_NOTIFICATION_PATH,
          e);
      return null;
    }
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
                "Inlaco Work Schedule Notification")
            .build();

    log.debug(
        "Sending schedule notification email to sailor [id={}, email={}]",
        profile.getId(),
        profile.getEmail());

    notificationFactory.sendNotificationAsync(NotificationType.EMAIL, emailRequest);
  }

  public void notifySailorSchedule(AssignedMobilization schedule) {
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
    String emailTemplate = loadEmailTemplate();
    if (emailTemplate == null) {
      return;
    }
    profiles.forEach(profile -> sendScheduleEmail(profile, schedule, emailTemplate));
  }
}

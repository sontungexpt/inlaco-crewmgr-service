package com.inlaco.crewmgrservice.feature.schedule.infrastructure.listener;

import com.inlaco.crewmgrservice.feature.crew.application.port.out.CrewProfileRepository;
import com.inlaco.crewmgrservice.feature.crew.domain.model.CrewProfile;
import com.inlaco.crewmgrservice.feature.notify.NotificationDispatcher;
import com.inlaco.crewmgrservice.feature.notify.NotificationPolicy;
import com.inlaco.crewmgrservice.feature.notify.mail.EmailRequest;
import com.inlaco.crewmgrservice.feature.notify.websocket.WebSocketNotificationPayload;
import com.inlaco.crewmgrservice.feature.notify.websocket.WebSocketNotificationRequest;
import com.inlaco.crewmgrservice.feature.schedule.domain.event.NewCrewMobilizationScheduleEvent;
import com.inlaco.crewmgrservice.feature.schedule.domain.model.CrewMobilizationSchedule;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Component
@Slf4j
@RequiredArgsConstructor
public class NewCrewMobilizationScheduleEventListener {

  private final CrewProfileRepository crewProfileRepository;
  private final NotificationDispatcher notificationDispatcher;
  private final SpringTemplateEngine templateEngine;

  @Value("${inlaco.client.base-url}")
  private String CLIENT_HOME_PAGE_LINK;

  @Value("${inlaco.template.email.sailor-schedule.path}")
  private String TEMPLATE_PATH;

  @Value("${inlaco.template.email.sailor-schedule.subject}")
  private String EMAIL_SUBJECT;

  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void handleNewAssignmentScheduleEvent(NewCrewMobilizationScheduleEvent event) {
    var schedule = event.schedule();
    // Use debug here because schedule events can be frequent; higher-level info is logged when
    // notifications are actually queued.
    log.debug("Handling schedule notification [id={}]", schedule.getId());
    notifySailorSchedule(schedule);
  }

  private void notifySailorSchedule(CrewMobilizationSchedule schedule) {
    if (schedule.getCrews() == null || schedule.getCrews().isEmpty()) {
      log.warn("Schedule {} has no crew members to notify", schedule.getId());
      return;
    }

    List<String> cardIds = schedule.getCrews().stream().map(it -> it.getEmployeeCardId()).toList();
    List<CrewProfile> profiles = crewProfileRepository.findAllByEmployeeCardId(cardIds);

    if (profiles.isEmpty()) {
      log.warn("No sailor profiles found for schedule {}", schedule.getId());
      return;
    }

    // Higher-level informational log indicating how many profiles will be processed for this
    // schedule.
    log.info(
        "Found {} sailor profile(s) to notify for schedule {}", profiles.size(), schedule.getId());

    profiles.forEach(profile -> sendScheduleEmail(profile, schedule));

    sendWebSocketNotification(profiles, schedule.getId());
  }

  record CrewMobilizationNotificationPayload(String message, String scheduleId)
      implements WebSocketNotificationPayload {

    @Override
    public String getMessage() {
      return message;
    }
  }

  private void sendWebSocketNotification(List<CrewProfile> profiles, String scheduleId) {
    log.info("Sending schedule notification to {} sailor(s)", profiles.size());
    List<String> recipientIds = profiles.stream().map(CrewProfile::getAccountId).toList();
    var payload = new CrewMobilizationNotificationPayload("Bạn có lịch điều động mới", scheduleId);
    notificationDispatcher.sendNotificationAsync(
        NotificationPolicy.WEB_SOCKET,
        new WebSocketNotificationRequest("SYSTEM", recipientIds, "/queue/notifications", payload));
  }

  private void sendScheduleEmail(CrewProfile profile, CrewMobilizationSchedule schedule) {
    if (profile.getEmail() == null || profile.getEmail().isBlank()) {
      log.warn("Skip notifying sailor {} due to missing email", profile.getId());
      return;
    }

    // Keep per-email send at debug to avoid noisy info logs; overall count is logged above at info
    // level.
    log.debug(
        "Sending schedule notification email to sailor [id={}, email={}, scheduleId={}]",
        profile.getId(),
        profile.getEmail(),
        schedule.getId());

    notificationDispatcher.sendNotificationAsync(
        NotificationPolicy.EMAIL,
        EmailRequest.html(profile.getEmail(), buildBodyContent(profile, schedule), EMAIL_SUBJECT)
            .build());
  }

  private String buildBodyContent(CrewProfile profile, CrewMobilizationSchedule schedule) {
    var context = new Context();
    context.setVariable("recipient_name", profile.getFullName());
    context.setVariable("company_name", "INLACO");
    context.setVariable("start_date", schedule.getStartDate().toString());
    context.setVariable("estimated_end_date", schedule.getEndDate().toString());
    context.setVariable("home_page_link", CLIENT_HOME_PAGE_LINK);
    context.setVariable("info_link", "");
    return templateEngine.process(TEMPLATE_PATH, context);
  }
}

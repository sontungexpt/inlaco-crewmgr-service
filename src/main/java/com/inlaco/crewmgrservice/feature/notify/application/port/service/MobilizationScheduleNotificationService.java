package com.inlaco.crewmgrservice.feature.notify.application.port.service;

import com.inlaco.crewmgrservice.feature.crew.application.port.out.CrewProfileRepository;
import com.inlaco.crewmgrservice.feature.crew.domain.model.CrewProfile;
import com.inlaco.crewmgrservice.feature.crewmobilization.domain.model.CrewMobilization;
import com.inlaco.crewmgrservice.feature.notify.application.port.in.CrewMobilizationNotificationUseCase;
import com.inlaco.crewmgrservice.feature.notify.application.port.out.DeviceTokenRepostiory;
import com.inlaco.crewmgrservice.feature.notify.application.port.out.NotificationRepository;
import com.inlaco.crewmgrservice.feature.notify.domain.enums.DeviceType;
import com.inlaco.crewmgrservice.feature.notify.domain.enums.NotificationType;
import com.inlaco.crewmgrservice.feature.notify.domain.model.DeviceToken;
import com.inlaco.crewmgrservice.feature.notify.domain.model.Notification;
import com.inlaco.crewmgrservice.feature.notify.domain.objectvalue.NewCrewMobilizationNotificationPayload;
import com.inlaco.crewmgrservice.feature.notify.sender.NotificationDispatcher;
import com.inlaco.crewmgrservice.feature.notify.sender.email.EmailRequest;
import com.inlaco.crewmgrservice.feature.notify.sender.pushnotification.ExpoNotificationRequest;
import com.inlaco.crewmgrservice.feature.notify.sender.websocket.WebSocketNotificationPayload;
import com.inlaco.crewmgrservice.feature.notify.sender.websocket.WebSocketNotificationRequest;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Service
@RequiredArgsConstructor
@Slf4j
public class MobilizationScheduleNotificationService
    implements CrewMobilizationNotificationUseCase {

  private final CrewProfileRepository crewProfileRepository;
  private final DeviceTokenRepostiory deviceTokenRepostiory;
  private final NotificationDispatcher notificationDispatcher;
  private final SpringTemplateEngine templateEngine;
  private final NotificationRepository notificationRepository;

  @Value("${inlaco.client.base-url}")
  private String CLIENT_HOME_PAGE_LINK;

  @Value("${inlaco.template.email.sailor-schedule.path}")
  private String TEMPLATE_PATH;

  @Value("${inlaco.template.email.sailor-schedule.subject}")
  private String EMAIL_SUBJECT;

  private static String TITLE = "Lịch điều động";

  private static String MESSAGE = "Bạn có lịch điều động mới. Vui lồng kiểm tra lịch điều động";

  @Override
  public void notifyUsers(CrewMobilization schedule) {
    log.debug("Handling schedule notification [id={}]", schedule.getId());

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

    log.info(
        "Found {} sailor profile(s) to notify for schedule {}", profiles.size(), schedule.getId());

    List<Notification> notifications =
        profiles.stream()
            .map(
                profile ->
                    Notification.builder()
                        .title(TITLE)
                        .recipientId(profile.getAccountId())
                        .message(MESSAGE)
                        .type(NotificationType.NEW_MOBILIZATION_SCHEDULE)
                        .payload(new NewCrewMobilizationNotificationPayload(schedule.getId()))
                        .build())
            .toList();

    notificationRepository.saveAll(notifications);

    sendEmail(profiles, schedule);

    sendWebSocketNotification(notifications, schedule.getId());

    sendPushNotification(profiles, schedule.getId());
  }

  record CrewMobilizationNotificationPayload(String title, String message, String scheduleId)
      implements WebSocketNotificationPayload {
    @Override
    public String getMessage() {
      return message;
    }
  }

  private void sendWebSocketNotification(List<Notification> notifications, String scheduleId) {
    log.info("Sending websocket notification for schedule {}", scheduleId);

    notifications.forEach(
        notification -> {
          notificationDispatcher.sendNotificationAsync(
              new WebSocketNotificationRequest(
                  notification.getRecipientId(), "/queue/notifications", notification));
        });
  }

  private void sendPushNotification(List<CrewProfile> profiles, String scheduleId) {
    // Collect all device tokens for the target users
    List<DeviceToken> tokens =
        profiles.stream()
            .flatMap(p -> deviceTokenRepostiory.findByUserId(p.getAccountId()).stream())
            .toList();

    if (tokens.isEmpty()) {
      log.info("No device tokens found for schedule {}", scheduleId);
      return;
    }

    List<String> expoTokens =
        tokens.stream()
            .filter(
                t -> t.getDeviceType() == DeviceType.ANDROID || t.getDeviceType() == DeviceType.IOS)
            .map(t -> t.getToken())
            .toList();

    if (expoTokens.isEmpty()) {
      log.info("No Expo tokens found for schedule {}", scheduleId);
      return;
    }
    var request =
        ExpoNotificationRequest.builder()
            .recipientTokens(expoTokens)
            .title(TITLE)
            .message(MESSAGE)
            .data(Map.of("scheduleId", scheduleId))
            .build();

    notificationDispatcher.sendNotificationAsync(request);
    log.info("Expo push notification dispatched to {} tokens", tokens.size());
  }

  private void sendEmail(List<CrewProfile> profiles, CrewMobilization schedule) {
    profiles.forEach(
        profile -> {
          if (profile.getEmail() == null || profile.getEmail().isBlank()) {
            log.warn("Skip notifying sailor {} due to missing email", profile.getId());
            return;
          }

          log.debug(
              "Sending schedule notification email to sailor [id={}, email={}, scheduleId={}]",
              profile.getId(),
              profile.getEmail(),
              schedule.getId());

          notificationDispatcher.sendNotificationAsync(
              EmailRequest.html(
                      profile.getEmail(), buildEmailContent(profile, schedule), EMAIL_SUBJECT)
                  .build());
        });
  }

  private String buildEmailContent(CrewProfile profile, CrewMobilization schedule) {
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

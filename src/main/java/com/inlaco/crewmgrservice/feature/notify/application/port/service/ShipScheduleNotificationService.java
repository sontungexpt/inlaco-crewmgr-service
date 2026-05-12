package com.inlaco.crewmgrservice.feature.notify.application.port.service;

import com.inlaco.crewmgrservice.feature.crew.domain.model.CrewProfile;
import com.inlaco.crewmgrservice.feature.notify.application.port.in.ShipScheduleNotificationUseCase;
import com.inlaco.crewmgrservice.feature.notify.application.port.out.DeviceTokenRepostiory;
import com.inlaco.crewmgrservice.feature.notify.application.port.out.NotificationRepository;
import com.inlaco.crewmgrservice.feature.notify.domain.enums.DeviceType;
import com.inlaco.crewmgrservice.feature.notify.domain.enums.NotificationType;
import com.inlaco.crewmgrservice.feature.notify.domain.model.DeviceToken;
import com.inlaco.crewmgrservice.feature.notify.domain.model.Notification;
import com.inlaco.crewmgrservice.feature.notify.domain.objectvalue.NewShipScheduleNotificationPayload;
import com.inlaco.crewmgrservice.feature.notify.sender.NotificationDispatcher;
import com.inlaco.crewmgrservice.feature.notify.sender.email.EmailRequest;
import com.inlaco.crewmgrservice.feature.notify.sender.pushnotification.ExpoNotificationRequest;
import com.inlaco.crewmgrservice.feature.notify.sender.websocket.WebSocketNotificationPayload;
import com.inlaco.crewmgrservice.feature.notify.sender.websocket.WebSocketNotificationRequest;
import com.inlaco.crewmgrservice.feature.shipschedule.domain.model.ShipSchedule;
import com.inlaco.crewmgrservice.feature.shipschedule.domain.model.ShipScheduleCrewAssignment;
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
public class ShipScheduleNotificationService implements ShipScheduleNotificationUseCase {

  private final DeviceTokenRepostiory deviceTokenRepostiory;
  private final NotificationDispatcher notificationDispatcher;
  private final SpringTemplateEngine templateEngine;
  private final NotificationRepository notificationRepository;

  @Value("${inlaco.client.base-url}")
  private String CLIENT_HOME_PAGE_LINK;

  @Value("${inlaco.template.email.ship-schedule.path}")
  private String TEMPLATE_PATH;

  @Value("${inlaco.template.email.ship-schedule.subject}")
  private String EMAIL_SUBJECT;

  private static String TITLE = "Lịch trình tàu mới";

  private static String MESSAGE =
      "Bạn có lịch trình tàu mới. Vui lòng kiểm tra lịch trình tàu của bạn.";

  @Override
  public void notifyUsers(
      ShipSchedule shipSchedule,
      List<ShipScheduleCrewAssignment> assignments,
      List<CrewProfile> crewProfiles) {
    log.debug("Handling ship schedule notification [id={}]", shipSchedule.getId());

    if (crewProfiles.isEmpty()) {
      log.warn("No sailor profiles found for ship schedule {}", shipSchedule.getId());
      return;
    }

    log.info(
        "Found {} sailor profile(s) to notify for ship schedule {}",
        crewProfiles.size(),
        shipSchedule.getId());

    List<Notification> notifications =
        crewProfiles.stream()
            .map(
                profile ->
                    Notification.builder()
                        .title(TITLE)
                        .recipientId(profile.getAccountId())
                        .message(MESSAGE)
                        .type(NotificationType.NEW_SHIP_SCHEDULE)
                        .payload(new NewShipScheduleNotificationPayload(shipSchedule.getId()))
                        .build())
            .toList();

    notificationRepository.saveAll(notifications);

    sendEmail(crewProfiles, shipSchedule);

    sendWebSocketNotification(notifications, shipSchedule.getId());

    sendPushNotification(crewProfiles, shipSchedule.getId());
  }

  record ShipScheduleNotificationPayload(String title, String message, String scheduleId)
      implements WebSocketNotificationPayload {
    @Override
    public String getMessage() {
      return message;
    }
  }

  private void sendWebSocketNotification(List<Notification> notifications, String scheduleId) {
    log.info("Sending websocket notification for ship schedule {}", scheduleId);

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
      log.info("No device tokens found for ship schedule {}", scheduleId);
      return;
    }

    List<String> expoTokens =
        tokens.stream()
            .filter(
                t -> t.getDeviceType() == DeviceType.ANDROID || t.getDeviceType() == DeviceType.IOS)
            .map(t -> t.getToken())
            .toList();

    if (expoTokens.isEmpty()) {
      log.info("No Expo tokens found for ship schedule {}", scheduleId);
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

  private void sendEmail(List<CrewProfile> profiles, ShipSchedule shipSchedule) {
    profiles.forEach(
        profile -> {
          if (profile.getEmail() == null || profile.getEmail().isBlank()) {
            log.warn("Skip notifying sailor {} due to missing email", profile.getId());
            return;
          }

          log.debug(
              "Sending ship schedule notification email to sailor [id={}, email={}, scheduleId={}]",
              profile.getId(),
              profile.getEmail(),
              shipSchedule.getId());

          notificationDispatcher.sendNotificationAsync(
              EmailRequest.html(
                      profile.getEmail(), buildEmailContent(profile, shipSchedule), EMAIL_SUBJECT)
                  .build());
        });
  }

  private String buildEmailContent(CrewProfile profile, ShipSchedule shipSchedule) {
    var context = new Context();
    context.setVariable("recipient_name", profile.getFullName());
    context.setVariable("company_name", "INLACO");
    context.setVariable("ship_name", shipSchedule.getShipInfo().getName());
    context.setVariable("departure_time", shipSchedule.getDepartureTime().toString());
    context.setVariable("arrival_time", shipSchedule.getArrivalTime().toString());
    context.setVariable("departure_port", shipSchedule.getDeparturePort());
    context.setVariable("arrival_port", shipSchedule.getArrivalPort());
    context.setVariable("route", shipSchedule.getRoute());
    context.setVariable("home_page_link", CLIENT_HOME_PAGE_LINK);
    context.setVariable("info_link", "");
    return templateEngine.process(TEMPLATE_PATH, context);
  }
}

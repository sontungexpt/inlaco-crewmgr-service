package com.inlaco.crewmgrservice.feature.notify.sender;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class NotificationDispatcher {

  private final List<NotificationSender<?, ?>> services;

  public NotificationDispatcher(List<NotificationSender<?, ?>> services) {
    this.services = services;
  }

  private <RQ extends NotificationRequest, RS> NotificationSender<RQ, RS> getService(RQ request) {
    NotificationSender<?, ?> service =
        services.stream()
            .filter(s -> s.supports(request))
            .findFirst()
            .orElseThrow(
                () ->
                    new IllegalArgumentException(
                        "Unsupported notification type: " + request.getClass()));

    log.debug("Resolved service: {}", service.getClass().getSimpleName());

    return (NotificationSender<RQ, RS>) service;
  }

  public <RQ extends NotificationRequest, RS> RS sendNotification(RQ request) {

    NotificationSender<RQ, RS> service = getService(request);
    RS result = service.sendNotification(request);
    log.info("Notification sent: {}", request.getClass().getSimpleName());
    return result;
  }

  public <RQ extends NotificationRequest, RS> CompletableFuture<RS> sendNotificationAsync(
      RQ request) {
    NotificationSender<RQ, RS> service = getService(request);
    log.info("Async notification triggered: {}", request.getClass().getSimpleName());
    return service.sendNotificationAsync(request);
  }
}

package com.inlaco.crewmgrservice.feature.notify;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class NotificationDispatcher {

  private final Map<NotificationPolicy, NotificationService> serviceMap;

  public NotificationDispatcher(List<NotificationService> notificationServices) {
    serviceMap = new EnumMap<>(NotificationPolicy.class);
    notificationServices.forEach(
        service -> {
          log.debug("Registering notification service for policy: {}", service.getPolicy());
          serviceMap.put(service.getPolicy(), service);
        });
    log.info("NotificationDispatcher initialized with {} services", serviceMap.size());
  }

  public <S, R> void sendNotification(
      NotificationPolicy policy, NotificationRequest<S, R> notificationRequest) {
    log.debug("Sending notification with policy: {}", policy);
    NotificationService service = serviceMap.get(policy);
    if (service == null) {
      log.warn("No notification service found for policy: {}", policy);
      throw new IllegalArgumentException("Unsupported notification policy: " + policy);
    }
    service.sendNotification(notificationRequest);
    log.info("Notification sent successfully with policy: {}", policy);
  }

  public <S, R> CompletableFuture<Void> sendNotificationAsync(
      NotificationPolicy policy, NotificationRequest<S, R> notificationRequest) {
    log.debug("Sending async notification with policy: {}", policy);
    NotificationService service = serviceMap.get(policy);
    if (service == null) {
      log.warn("No notification service found for policy: {}", policy);
      throw new IllegalArgumentException("Unsupported notification policy: " + policy);
    }
    log.info("Async notification initiated with policy: {}", policy);
    return service.sendNotificationAsync(notificationRequest);
  }
}

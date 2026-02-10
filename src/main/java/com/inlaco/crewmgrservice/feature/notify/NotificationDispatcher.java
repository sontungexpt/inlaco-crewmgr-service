package com.inlaco.crewmgrservice.feature.notify;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class NotificationDispatcher {

  private final Map<NotificationPolicy, NotificationService> serviceMap;

  public NotificationDispatcher(List<NotificationService> notificationServices) {
    serviceMap =
        notificationServices.stream()
            .collect(
                Collectors.toMap(
                    NotificationService::getPolicy, notificationService -> notificationService));
  }

  public <S, R> void sendNotification(
      NotificationPolicy policy, NotificationRequest<S, R> notificationRequest) {
    serviceMap.get(policy).sendNotification(notificationRequest);
  }

  public <S, R> CompletableFuture<Void> sendNotificationAsync(
      NotificationPolicy policy, NotificationRequest<S, R> notificationRequest) {
    return serviceMap.get(policy).sendNotificationAsync(notificationRequest);
  }
}

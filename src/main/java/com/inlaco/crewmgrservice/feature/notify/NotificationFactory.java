package com.inlaco.crewmgrservice.feature.notify;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@SuppressWarnings({"rawtypes", "unchecked"})
public class NotificationFactory {

  /**
   * A map that contains NotificationService instances mapped to their corresponding notification
   * types.
   */
  private final Map<String, NotificationService> notificationServiceMap;

  /**
   * Returns the NotificationService instance corresponding to the provided notification type.
   *
   * @param notificationType the type of notification
   * @return the NotificationService instance corresponding to the provided notification type
   * @throws IllegalArgumentException if the provided notification type is not supported
   */
  public NotificationService getNotificationService(String notificationType) {
    var notificationService = notificationServiceMap.get(notificationType);
    if (notificationService == null) {
      throw new IllegalArgumentException("Unsupported notification type " + notificationType);
    }
    return notificationService;
  }

  /**
   * Executes the sendNotification() method on the NotificationService instance corresponding to the
   * provided notification type.
   *
   * @param notificationType the type of notification to execute
   * @throws IllegalArgumentException if the provided notification type is not supported
   */
  public <S, R> void sendNotification(
      String notificationType, NotificationRequest<S, R> notificationRequest) {
    var notificationService = getNotificationService(notificationType);
    notificationService.sendNotification(notificationRequest);
  }

  /**
   * Executes the sendNotification() method on the NotificationService instance corresponding to the
   * provided notification type asynchronously.
   *
   * @param notificationType the type of notification to execute
   * @throws IllegalArgumentException if the provided notification type is not supported
   */
  public <S, R> CompletableFuture<Void> sendNotificationAsync(
      String notificationType, NotificationRequest<S, R> notificationRequest) {
    var notificationService = getNotificationService(notificationType);
    return notificationService.sendNotificationAsync(notificationRequest);
  }
}

package com.inlaco.crewmgrservice.feature.notify;

import java.util.concurrent.CompletableFuture;

public interface NotificationService<N extends NotificationRequest<?, ?>> {

  NotificationPolicy getPolicy();

  void sendNotification(N request);

  default CompletableFuture<Void> sendNotificationAsync(N request) {
    return CompletableFuture.runAsync(
        () -> {
          try {
            sendNotification(request);
          } catch (Exception e) {
            e.printStackTrace();
          }
        });
  }
}

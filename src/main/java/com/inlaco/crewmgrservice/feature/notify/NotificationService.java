package com.inlaco.crewmgrservice.feature.notify;

import java.util.concurrent.CompletableFuture;

public interface NotificationService<N extends NotificationRequest<?, ?>> {

  void sendNotification(N request);

  default CompletableFuture<Void> sendNotificationAsync(N request) {
    return CompletableFuture.runAsync(() -> sendNotification(request));
  }
}

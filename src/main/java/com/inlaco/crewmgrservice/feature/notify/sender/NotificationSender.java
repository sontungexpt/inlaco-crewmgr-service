package com.inlaco.crewmgrservice.feature.notify.sender;

import java.util.concurrent.CompletableFuture;

public interface NotificationSender<RQ extends NotificationRequest, RS> {

  Class<RQ> getRequestType();

  default boolean supports(NotificationRequest request) {
    return getRequestType().isAssignableFrom(request.getClass());
  }

  RS sendNotification(RQ request);

  default CompletableFuture<RS> sendNotificationAsync(RQ request) {
    return CompletableFuture.supplyAsync(
        () -> {
          try {
            return sendNotification(request);
          } catch (Exception e) {
            throw new RuntimeException(e);
          }
        });
  }
}

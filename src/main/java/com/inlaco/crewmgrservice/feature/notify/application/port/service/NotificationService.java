package com.inlaco.crewmgrservice.feature.notify.application.port.service;

import com.inlaco.crewmgrservice.feature.notify.domain.model.NotificationRequest;
import java.util.concurrent.CompletableFuture;

public interface NotificationService<RQ extends NotificationRequest, RS> {

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

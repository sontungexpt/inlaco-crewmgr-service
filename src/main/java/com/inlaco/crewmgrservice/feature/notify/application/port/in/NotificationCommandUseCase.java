package com.inlaco.crewmgrservice.feature.notify.application.port.in;

public interface NotificationCommandUseCase {

  void markAsRead(String userId, String notificationId);

  void markAllAsRead(String userId);
}

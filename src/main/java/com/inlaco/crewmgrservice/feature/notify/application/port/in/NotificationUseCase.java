package com.inlaco.crewmgrservice.feature.notify.application.port.in;

import com.inlaco.crewmgrservice.feature.notify.domain.model.Notification;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NotificationUseCase {

  void createBulk(
      List<String> recipientIds, String title, String message, String type, String referenceId);

  Page<Notification> getUserNotifications(String userId, Pageable pageable);

  long countUnread(String userId);

  void markAsRead(String userId, String notificationId);

  void markAllAsRead(String userId);
}

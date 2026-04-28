package com.inlaco.crewmgrservice.feature.notify.application.port.out;

import com.inlaco.crewmgrservice.feature.notify.domain.model.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NotificationRepository {

  Notification save(Notification notification);

  Page<Notification> findByRecipientId(String recipientId, Pageable pageable);

  long countUnread(String recipientId);

  void markAsRead(String notificationId, String recipientId);

  void markAllAsRead(String recipientId);
}

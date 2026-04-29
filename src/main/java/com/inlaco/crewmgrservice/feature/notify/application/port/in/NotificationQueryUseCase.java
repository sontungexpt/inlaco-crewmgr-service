package com.inlaco.crewmgrservice.feature.notify.application.port.in;

import com.inlaco.crewmgrservice.feature.notify.domain.model.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NotificationQueryUseCase {

  Page<Notification> getNotifications(String userId, Pageable pageable);

  Notification getNotification(String notificationId, String userId);

  long countUnread(String userId);
}

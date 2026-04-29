package com.inlaco.crewmgrservice.feature.notify.application.port.out;

import com.inlaco.crewmgrservice.feature.notify.domain.model.Notification;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NotificationRepository {

  Notification save(Notification notification);

  List<Notification> saveAll(Iterable<Notification> notifications);

  Page<Notification> findByRecipientId(String recipientId, Pageable pageable);

  Optional<Notification> findByIdAndRecipientId(String notificationId, String recipientId);

  long countUnread(String recipientId);

  void markAsRead(String notificationId, String recipientId);

  void markAllAsRead(String recipientId);
}

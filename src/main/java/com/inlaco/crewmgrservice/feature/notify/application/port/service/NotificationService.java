package com.inlaco.crewmgrservice.feature.notify.application.port.service;

import com.inlaco.crewmgrservice.feature.notify.application.port.in.NotificationCommandUseCase;
import com.inlaco.crewmgrservice.feature.notify.application.port.in.NotificationQueryUseCase;
import com.inlaco.crewmgrservice.feature.notify.application.port.out.NotificationRepository;
import com.inlaco.crewmgrservice.feature.notify.domain.model.Notification;
import com.inlaco.crewmgrservice.shared.kernel.exception.ResourceNotFoundException;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService implements NotificationQueryUseCase, NotificationCommandUseCase {

  private final NotificationRepository repository;

  @Override
  public Page<Notification> getNotifications(String userId, Pageable pageable) {
    return repository.findByRecipientId(userId, pageable);
  }

  @Override
  public Notification getNotification(String notificationId, String userId) {
    return repository
        .findByIdAndRecipientId(notificationId, userId)
        .orElseThrow(
            () ->
                new ResourceNotFoundException(
                    Notification.class, Map.of("id", notificationId, "recipientId", userId)));
  }

  @Override
  public long countUnread(String userId) {
    return repository.countUnread(userId);
  }

  @Override
  public void markAsRead(String userId, String notificationId) {
    repository.markAsRead(notificationId, userId);
  }

  @Override
  public void markAllAsRead(String userId) {
    repository.markAllAsRead(userId);
  }
}

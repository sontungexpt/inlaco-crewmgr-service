package com.inlaco.crewmgrservice.feature.notify.application.port.service;

import com.inlaco.crewmgrservice.feature.notify.application.port.in.NotificationUseCase;
import com.inlaco.crewmgrservice.feature.notify.application.port.out.NotificationRepository;
import com.inlaco.crewmgrservice.feature.notify.domain.model.Notification;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService implements NotificationUseCase {

  private final NotificationRepository repository;

  @Override
  public void createBulk(
      List<String> recipientIds, String title, String message, String type, String referenceId) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'createBulk'");
  }

  @Override
  public Page<Notification> getUserNotifications(String userId, Pageable pageable) {
    return repository.findByRecipientId(userId, pageable);
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

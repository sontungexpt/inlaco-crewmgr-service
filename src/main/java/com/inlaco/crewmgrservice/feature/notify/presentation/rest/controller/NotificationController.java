package com.inlaco.crewmgrservice.feature.notify.presentation.rest.controller;

import com.inlaco.crewmgrservice.feature.notify.application.port.in.NotificationCommandUseCase;
import com.inlaco.crewmgrservice.feature.notify.application.port.in.NotificationQueryUseCase;
import com.inlaco.crewmgrservice.feature.notify.domain.model.Notification;
import com.inlaco.crewmgrservice.feature.user.domain.model.User;
import com.inlaco.crewmgrservice.infrastructure.web.annotation.CurrentUser;
import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notifications")
public class NotificationController {

  private final NotificationQueryUseCase notificationUseCase;
  private final NotificationCommandUseCase notificationCommandUseCase;

  @RolesAllowed("USER")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PutMapping("/{id}/read")
  public void markAsRead(@CurrentUser User user, @PathVariable String id) {
    notificationCommandUseCase.markAsRead(user.getId(), id);
  }

  @RolesAllowed("USER")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PutMapping("/read-all")
  public void markAll(@CurrentUser User user) {
    notificationCommandUseCase.markAllAsRead(user.getId());
  }

  @GetMapping("/unread-count")
  @RolesAllowed("USER")
  @ResponseStatus(HttpStatus.OK)
  public long countUnread(@CurrentUser User user) {
    return notificationUseCase.countUnread(user.getId());
  }

  @GetMapping("")
  @RolesAllowed("USER")
  @ResponseStatus(HttpStatus.OK)
  public Page<Notification> getNotifications(
      @PageableDefault Pageable pageable, @CurrentUser User user) {
    return notificationUseCase.getNotifications(user.getId(), pageable);
  }

  @GetMapping("/{id}")
  @RolesAllowed("USER")
  @ResponseStatus(HttpStatus.OK)
  public Notification getNotification(@RequestParam String id, @CurrentUser User user) {
    return notificationUseCase.getNotification(id, user.getId());
  }
}

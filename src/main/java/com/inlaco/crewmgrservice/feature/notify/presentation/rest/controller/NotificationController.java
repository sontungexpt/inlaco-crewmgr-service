package com.inlaco.crewmgrservice.feature.notify.presentation.rest.controller;

import com.inlaco.crewmgrservice.feature.notify.application.port.in.NotificationUseCase;
import com.inlaco.crewmgrservice.feature.notify.domain.model.Notification;
import com.inlaco.crewmgrservice.feature.user.domain.model.User;
import com.inlaco.crewmgrservice.infrastructure.web.annotation.CurrentUser;
import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notifications")
public class NotificationController {

  private final NotificationUseCase notificationUseCase;

  @PostMapping("/{id}/read")
  @RolesAllowed("USER")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void markAsRead(@CurrentUser User user, @PathVariable String id) {
    notificationUseCase.markAsRead(user.getId(), id);
  }

  @PostMapping("/read-all")
  @RolesAllowed("USER")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void markAll(@CurrentUser User user) {
    notificationUseCase.markAllAsRead(user.getId());
  }

  @PostMapping("/unread-count")
  @RolesAllowed("USER")
  @ResponseStatus(HttpStatus.OK)
  public void countUnread(@CurrentUser User user) {
    notificationUseCase.countUnread(user.getId());
  }

  @PostMapping("")
  @RolesAllowed("USER")
  @ResponseStatus(HttpStatus.OK)
  public Page<Notification> getNotifications(
      @PageableDefault Pageable pageable, @CurrentUser User user) {
    return notificationUseCase.getUserNotifications(user.getId(), pageable);
  }
}

package com.inlaco.crewmgrservice.feature.notify.domain.model;

import com.inlaco.crewmgrservice.feature.notify.domain.enums.NotificationLevel;
import com.inlaco.crewmgrservice.feature.notify.domain.enums.NotificationType;
import java.time.Instant;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;

@Getter
@Builder
public class Notification {

  private String id;

  private String recipientId;

  private String title;
  private String message;

  private NotificationType type;

  private NotificationLevel level;

  private NotificationPayload payload;

  @Default private boolean read = false;

  private Instant createdAt;
}

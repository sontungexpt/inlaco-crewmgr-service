package com.inlaco.crewmgrservice.feature.notify.domain.model;

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

  private String type; // SCHEDULE, SYSTEM,...

  @Default private boolean read = false;

  private Instant createdAt;
}

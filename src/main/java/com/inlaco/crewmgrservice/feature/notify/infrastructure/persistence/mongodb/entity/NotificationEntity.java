package com.inlaco.crewmgrservice.feature.notify.infrastructure.persistence.mongodb.entity;

import com.inlaco.crewmgrservice.feature.notify.domain.enums.NotificationLevel;
import com.inlaco.crewmgrservice.feature.notify.domain.enums.NotificationType;
import com.inlaco.crewmgrservice.feature.notify.domain.model.NotificationPayload;
import java.time.Instant;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "notifications")
@CompoundIndex(name = "recipient_read_idx", def = "{'recipientId': 1, 'read': 1}")
public class NotificationEntity {

  @Id private String id;

  private ObjectId recipientId;

  private String title;
  private String message;

  private NotificationType type;
  private NotificationLevel level;

  private boolean read = false;

  private NotificationPayload payload;

  @CreatedDate private Instant createdAt;

  @LastModifiedDate private Instant updatedAt;
}

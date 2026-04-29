package com.inlaco.crewmgrservice.feature.notify.infrastructure.persistence.mongodb.entity;

import java.time.Instant;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "notifications")
public class NotificationEntity {

  @Id private String id;

  private ObjectId recipientId;

  private String title;
  private String message;

  private String type;

  private boolean read = false;

  @CreatedDate private Instant createdAt;

  @LastModifiedDate private Instant updatedAt;
}

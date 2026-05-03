package com.inlaco.crewmgrservice.feature.notify.infrastructure.persistence.mongodb.entity;

import com.inlaco.crewmgrservice.feature.notify.domain.enums.DeviceType;
import java.time.Instant;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "device_tokens")
public class DeviceTokenEntity {

  @Id private String id;

  private ObjectId userId;

  private String token;

  private DeviceType deviceType;

  @CreatedDate private Instant createdAt;

  @LastModifiedDate private Instant updatedAt;
}

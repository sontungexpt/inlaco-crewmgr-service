package com.inlaco.crewmgrservice.feature.attendance.infrastructure.persistence.mongodb.entity;

import com.inlaco.crewmgrservice.feature.attendance.domain.enums.AttendanceMethod;
import com.inlaco.crewmgrservice.feature.attendance.domain.enums.AttendanceStatus;
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
@Document(collection = "attendances")
public class AttendanceEntity {

  @Id private String id;

  private ObjectId userId;

  private ObjectId scheduleId;

  private Instant checkInAt;

  private Instant checkOutAt;

  private AttendanceStatus status;

  private AttendanceMethod checkInMethod; // QR, GPS
  private AttendanceMethod checkOutMethod;

  private String location;

  @CreatedDate private Instant createdAt;

  @LastModifiedDate private Instant updatedAt;
}

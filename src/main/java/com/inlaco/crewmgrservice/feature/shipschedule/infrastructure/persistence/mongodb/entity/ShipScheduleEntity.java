package com.inlaco.crewmgrservice.feature.shipschedule.infrastructure.persistence.mongodb.entity;

import com.inlaco.crewmgrservice.feature.shipschedule.domain.enums.ScheduleStatus;
import com.inlaco.crewmgrservice.shared.objectvalue.ShipInfo;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "ship_schedules")
public class ShipScheduleEntity {

  @Id private String id; // MongoDB will auto-generate this

  private String vesselOwnerId;

  private ShipInfo shipInfo;

  private String route;

  private Instant departureTime;
  private Instant arrivalTime;

  private String departurePort;
  private String arrivalPort;

  private int totalCrews;

  private ScheduleStatus status;

  @CreatedBy private ObjectId createdBy;
  @CreatedDate private Instant createdAt;
  @LastModifiedBy private ObjectId updatedBy;
  @LastModifiedDate private Instant updatedAt;
}

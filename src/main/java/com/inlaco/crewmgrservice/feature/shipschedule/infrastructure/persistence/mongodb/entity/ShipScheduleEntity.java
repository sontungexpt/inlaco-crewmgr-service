package com.inlaco.crewmgrservice.feature.shipschedule.infrastructure.persistence.mongodb.entity;

import com.inlaco.crewmgrservice.feature.shipschedule.domain.enums.ScheduleStatus;
import java.time.Instant;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "ship_schedules")
public class ShipScheduleEntity {
  
  @Id
  private String id; // MongoDB will auto-generate this
  private String clientId;
  private String shipImo;
  private String shipName;
  private String route;
  private Instant departureTime;
  private Instant arrivalTime;
  private String departurePort;
  private String arrivalPort;
  private ScheduleStatus status;
  private List<String> employeeCardIds;
  private String createdBy;
  private Instant createdAt;
  private String updatedBy;
  private Instant updatedAt;
}

package com.inlaco.crewmgrservice.feature.shipschedule.presentation.dto.response;

import com.inlaco.crewmgrservice.feature.shipschedule.domain.enums.ScheduleStatus;
import java.time.Instant;
import java.util.List;
import lombok.Data;

@Data
public class ShipScheduleResponse {
  
  private String id;
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

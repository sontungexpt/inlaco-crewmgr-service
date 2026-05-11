package com.inlaco.crewmgrservice.feature.shipschedule.presentation.dto.response;

import com.inlaco.crewmgrservice.feature.shipschedule.domain.enums.ScheduleStatus;
import com.inlaco.crewmgrservice.shared.objectvalue.ShipInfoResponse;
import java.time.Instant;
import lombok.Data;

@Data
public class ShipScheduleResponse {

  private String id;
  private String vesselOwnerId;
  private ShipInfoResponse shipInfo;
  private String route;
  private Instant departureTime;
  private Instant arrivalTime;
  private String departurePort;
  private String arrivalPort;
  private ScheduleStatus status;
  private String createdBy;
  private Instant createdAt;
  private String updatedBy;
  private Instant updatedAt;
}

package com.inlaco.crewmgrservice.feature.shipschedule.application.model;

import com.inlaco.crewmgrservice.feature.shipschedule.domain.enums.ScheduleStatus;
import com.inlaco.crewmgrservice.shared.objectvalue.ShipInfoResponse;
import java.time.Instant;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShipScheduleDetail {

  private String id;
  private String clientId;

  private ShipInfoResponse shipInfo;

  private String route;
  private Instant departureTime;
  private Instant arrivalTime;

  private String departurePort;
  private String arrivalPort;

  private ScheduleStatus status;

  private List<ShipScheduleAssignedCrewDetail> crews;

  private String createdBy;
  private Instant createdAt;

  private String updatedBy;
  private Instant updatedAt;
}

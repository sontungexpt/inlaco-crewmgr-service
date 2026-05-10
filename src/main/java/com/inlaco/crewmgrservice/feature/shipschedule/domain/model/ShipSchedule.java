package com.inlaco.crewmgrservice.feature.shipschedule.domain.model;

import com.inlaco.crewmgrservice.feature.shipschedule.domain.enums.ScheduleStatus;
import com.inlaco.crewmgrservice.shared.objectvalue.ShipInfo;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShipSchedule {
  private String id;

  private String vesselOwnerId;

  private ShipInfo shipInfo;

  private String route;

  private Instant departureTime;
  private Instant arrivalTime;

  private String departurePort;
  private String arrivalPort;

  @Builder.Default private ScheduleStatus status = ScheduleStatus.DRAFT;

  private String createdBy;
  private Instant createdAt;

  private String updatedBy;
  private Instant updatedAt;

  public void changeStatus(ScheduleStatus newStatus) throws IllegalStateException {
    if (status == newStatus) return;
    status.validateTransition(newStatus);
    status = newStatus;
  }
}

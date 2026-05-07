package com.inlaco.crewmgrservice.feature.shipschedule.presentation.dto.request;

import com.inlaco.crewmgrservice.feature.shipschedule.domain.enums.ScheduleStatus;
import jakarta.validation.Valid;
import java.time.Instant;
import java.util.List;
import lombok.Data;

@Data
public class UpdateShipScheduleRequest {

  private String clientId;
  private String shipImo;
  private String shipName;
  private String route;
  private Instant departureTime;
  private Instant arrivalTime;
  private String departurePort;
  private String arrivalPort;
  private ScheduleStatus status;

  @Valid private List<String> employeeCardIds;

  private String updatedBy;
}

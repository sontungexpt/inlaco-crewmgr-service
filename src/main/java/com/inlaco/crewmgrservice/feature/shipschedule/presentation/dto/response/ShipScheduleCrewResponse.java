package com.inlaco.crewmgrservice.feature.shipschedule.presentation.dto.response;

import com.inlaco.crewmgrservice.feature.crew.presentation.dto.response.CrewProfileResponse;
import com.inlaco.crewmgrservice.feature.shipschedule.domain.enums.ScheduleStatus;
import java.time.Instant;
import java.util.List;
import lombok.Data;

@Data
public class ShipScheduleCrewResponse {
  
  private String id;
  private String clientId;
  private String shipId;
  private String shipName;
  private String route;
  private Instant departureTime;
  private Instant arrivalTime;
  private String departurePort;
  private String arrivalPort;
  private ScheduleStatus status;
  private List<CrewProfileResponse> crewMembers;
  private Instant createdAt;
  private Instant updatedAt;
}

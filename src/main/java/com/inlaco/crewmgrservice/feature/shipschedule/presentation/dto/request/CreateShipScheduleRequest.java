package com.inlaco.crewmgrservice.feature.shipschedule.presentation.dto.request;

import com.inlaco.crewmgrservice.feature.shipschedule.domain.enums.ScheduleStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.List;
import lombok.Data;

@Data
public class CreateShipScheduleRequest {
  
  @NotBlank(message = "Client ID is required")
  private String clientId;
  
  @NotBlank(message = "Ship IMO is required")
  private String shipImo;
  
  @NotBlank(message = "Ship name is required")
  private String shipName;
  
  private String route;
  
  @NotNull(message = "Departure time is required")
  private Instant departureTime;
  
  private Instant arrivalTime;
  
  @NotBlank(message = "Departure port is required")
  private String departurePort;
  
  private String arrivalPort;
  
  private ScheduleStatus status = ScheduleStatus.DRAFT;
  
  @Valid
  private List<String> employeeCardIds;
}

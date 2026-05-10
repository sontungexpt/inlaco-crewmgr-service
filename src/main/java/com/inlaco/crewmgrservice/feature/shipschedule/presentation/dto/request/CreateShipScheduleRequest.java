package com.inlaco.crewmgrservice.feature.shipschedule.presentation.dto.request;

import com.inlaco.crewmgrservice.feature.crewrental.presentation.dto.ShipInfoRequest;
import com.inlaco.crewmgrservice.feature.shipschedule.domain.enums.ScheduleStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.Instant;
import java.util.List;
import lombok.Data;

@Data
public class CreateShipScheduleRequest {

  @Valid @NotNull private ShipInfoRequest shipInfo;

  @NotNull(message = "Departure time is required")
  private Instant departureTime;

  @NotNull(message = "Arrival time is required")
  private Instant arrivalTime;

  @NotBlank(message = "Departure port is required")
  private String departurePort;

  @NotBlank(message = "Arrival port is required")
  private String arrivalPort;

  private ScheduleStatus status = ScheduleStatus.DRAFT;

  @Size(min = 1, message = "At least one crew member is required")
  private List<@Valid AssignedCrew> crews;

  public static record AssignedCrew(
      @NotBlank(message = "Employee card ID is required") String employeeCardId,
      @NotBlank(message = "Rank on board is required") String rankOnBoard) {}
}

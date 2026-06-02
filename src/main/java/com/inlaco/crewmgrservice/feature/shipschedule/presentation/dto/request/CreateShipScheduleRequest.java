package com.inlaco.crewmgrservice.feature.shipschedule.presentation.dto.request;

import com.inlaco.crewmgrservice.feature.crewrental.presentation.dto.ShipInfoRequest;
import com.inlaco.crewmgrservice.feature.shipschedule.domain.enums.ScheduleStatus;
import com.inlaco.crewmgrservice.infrastructure.web.payload.request.constraint.TimeFrame;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.Instant;
import java.util.List;
import lombok.Data;

@Data
public class CreateShipScheduleRequest {

  @NotNull private ShipInfoRequest shipInfo;

  @NotNull(message = "Departure time is required")
  private Instant departureTime;

  @NotNull(message = "Arrival time is required")
  private Instant arrivalTime;

  @NotBlank(message = "Route is required")
  private String route;

  @NotBlank(message = "Departure port is required")
  private String departurePort;

  @NotBlank(message = "Arrival port is required")
  private String arrivalPort;

  private ScheduleStatus status = ScheduleStatus.DRAFT;

  @Size(min = 1, message = "At least one crew member is required")
  private List<@Valid AssignedCrew> crews;

  public static record AssignedCrew(
      @NotBlank(message = "Employee card ID is required") String employeeCardId,
      @NotBlank(message = "Rank on board is required") String rankOnBoard,
      Instant boardingTime,
      Instant disembarkTime,
      @NotBlank(message = "Boarding port is required") String boardingPort,
      @NotBlank(message = "Disembark port is required") String disembarkPort,
      String note)
      implements TimeFrame {

    @Override
    public List<Range> getTimeFrames() {
      return List.of(Range.bothRequired(boardingTime, disembarkTime));
    }
  }
}

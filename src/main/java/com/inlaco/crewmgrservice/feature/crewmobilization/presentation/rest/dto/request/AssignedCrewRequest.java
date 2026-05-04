package com.inlaco.crewmgrservice.feature.crewmobilization.presentation.rest.dto.request;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.inlaco.crewmgrservice.infrastructure.web.payload.request.constraint.TimeFrame;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.List;

public record AssignedCrewRequest(
    @JsonAlias("employeeCardId") @NotBlank String employeeCardId,
    String fullName,
    @NotBlank String rankOnBoard,
    @NotNull Instant startDate,
    @NotNull Instant endDate)
    implements TimeFrame {

  @Override
  public List<Range> getTimeFrames() {
    return List.of(Range.bothRequired(startDate, endDate));
  }
}

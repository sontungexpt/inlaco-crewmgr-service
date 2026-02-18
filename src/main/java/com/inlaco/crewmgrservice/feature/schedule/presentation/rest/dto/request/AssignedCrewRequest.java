package com.inlaco.crewmgrservice.feature.schedule.presentation.rest.dto.request;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.inlaco.crewmgrservice.infrastructure.web.payload.request.constraint.TimeFrame;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.List;

public record AssignedCrewRequest(
    String id,
    @JsonAlias({"employeeCardId", "cardId"}) @NotBlank String employeeCardId,
    @NotBlank String rankOnBoard,
    @NotNull Instant startDate,
    @NotNull Instant endDate)
    implements TimeFrame {

  @Override
  public List<Range> getTimeFrames() {
    return List.of(Range.of(startDate, endDate));
  }
}

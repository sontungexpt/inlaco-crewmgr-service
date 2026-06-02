package com.inlaco.crewmgrservice.feature.crewmobilization.presentation.rest.dto.request;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.inlaco.crewmgrservice.infrastructure.web.payload.request.constraint.TimeFrame;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.List;

public record AssignedCrewRequest(
    @JsonAlias("employeeCardId") @NotBlank String employeeCardId,
    @NotBlank String rankOnBoard,
    @NotNull Instant startDate,
    @NotNull Instant endDate)
    implements TimeFrame {

  @Override
  public List<Range> getTimeFrames() {
    return List.of(Range.bothRequired(startDate, endDate));
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((employeeCardId == null) ? 0 : employeeCardId.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    AssignedCrewRequest other = (AssignedCrewRequest) obj;
    if (employeeCardId == null) {
      if (other.employeeCardId != null) return false;
    } else if (!employeeCardId.equals(other.employeeCardId)) return false;
    return true;
  }
}

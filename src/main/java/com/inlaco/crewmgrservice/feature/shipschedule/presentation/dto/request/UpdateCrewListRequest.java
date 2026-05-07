package com.inlaco.crewmgrservice.feature.shipschedule.presentation.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import lombok.Data;

@Data
public class UpdateCrewListRequest {

  @Valid
  @NotEmpty(message = "Employee card IDs list cannot be empty")
  private List<String> employeeCardIds;
}

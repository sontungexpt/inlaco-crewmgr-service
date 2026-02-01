package com.inlaco.crewmgrservice.feature.schedule.dto;

import com.inlaco.crewmgrservice.common.payload.Filterable;
import com.inlaco.crewmgrservice.feature.schedule.model.AssignedMobilization;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleFilterable implements Filterable {

  private AssignedMobilization.Status status;

  private Instant startDate;

  private Instant endDate;
}

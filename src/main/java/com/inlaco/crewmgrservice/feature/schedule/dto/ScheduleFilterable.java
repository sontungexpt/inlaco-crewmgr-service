package com.inlaco.crewmgrservice.feature.schedule.dto;

import com.inlaco.crewmgrservice.common.payload.Filterable;
import com.inlaco.crewmgrservice.feature.schedule.model.AssigmentSchedule;
import java.time.Instant;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class ScheduleFilterable implements Filterable {

  private AssigmentSchedule.Status status;

  private Instant startDate;

  private Instant estimatedEndDate;
}

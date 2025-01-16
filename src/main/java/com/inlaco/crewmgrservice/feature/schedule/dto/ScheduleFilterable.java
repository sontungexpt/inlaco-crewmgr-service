package com.inlaco.crewmgrservice.feature.schedule.dto;

import com.inlaco.crewmgrservice.common.payload.Filterable;
import com.inlaco.crewmgrservice.feature.schedule.model.Schedule;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ScheduleFilterable implements Filterable {

  private Schedule.Status status;
}

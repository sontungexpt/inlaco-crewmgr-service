package com.inlaco.crewmgrservice.feature.schedule.application.model;

import com.inlaco.crewmgrservice.feature.schedule.domain.enums.CrewMobilizationScheduleStatus;
import java.time.Instant;
import lombok.Data;

@Data
public class CrewMobilizationScheduleSearchCriteria {

  private String keyword;
  private String accountId;
  private CrewMobilizationScheduleStatus status;
  private Instant startDate;
  private Instant endDate;
}

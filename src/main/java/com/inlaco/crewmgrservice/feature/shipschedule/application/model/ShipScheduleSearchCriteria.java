package com.inlaco.crewmgrservice.feature.shipschedule.application.model;

import com.inlaco.crewmgrservice.feature.shipschedule.domain.enums.ScheduleStatus;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShipScheduleSearchCriteria {

  private String keyword;
  private String clientId;
  private String shipIMO;
  private ScheduleStatus status;
  private Instant departureStartTime;
  private Instant departureEndTime;
}

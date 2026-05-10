package com.inlaco.crewmgrservice.feature.shipschedule.application.model;

import com.inlaco.crewmgrservice.feature.shipschedule.domain.enums.ScheduleStatus;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(
    of = {
      "keyword",
      "vesselOwnerId",
      "shipIMO",
      "status",
      "departureStartTime",
      "departureEndTime"
    })
public class ShipScheduleSearchCriteria {

  private String keyword;
  private String vesselOwnerId;
  private String shipIMO;
  private ScheduleStatus status;
  private Instant departureStartTime;
  private Instant departureEndTime;
}

package com.inlaco.crewmgrservice.feature.shipschedule.application.model;

import com.inlaco.crewmgrservice.feature.shipschedule.domain.enums.CheckType;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceLogSearchCriteria {
  private String shipScheduleId;
  private String crewAccountId;
  private String keyword;
  private CheckType checkType;
  private String location;
  private Instant startTime;
  private Instant endTime;
}

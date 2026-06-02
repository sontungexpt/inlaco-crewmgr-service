package com.inlaco.crewmgrservice.feature.shipschedule.presentation.dto.response;

import com.inlaco.crewmgrservice.feature.shipschedule.domain.enums.AttendanceMethod;
import com.inlaco.crewmgrservice.feature.shipschedule.domain.enums.CheckType;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class AttendanceLogResponse {

  private String id;

  private String crewEmployeeCardId;
  private String crewProfileId;
  private String crewName;
  private String crewRankOnBoard;

  private String shipScheduleId;

  private Instant timestamp;

  private CheckType checkType;

  private AttendanceMethod method;

  private String location;

  private String note;

  private Instant createdAt;
}

package com.inlaco.crewmgrservice.feature.shipschedule.domain.model;

import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShipScheduleCrewAssignment {
  private String id;

  private String scheduleId;

  private String profileId;
  private String accountId;
  private String employeeCardId;

  private String rankOnBoard;

  private Instant onboardedAt;
  private Instant offboardedAt;

  // This is snapshot information for legitimate crew member
  private String fullName;
  private String note;
}

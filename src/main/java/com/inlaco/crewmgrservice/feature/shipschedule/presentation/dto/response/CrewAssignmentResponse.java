package com.inlaco.crewmgrservice.feature.shipschedule.presentation.dto.response;

import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CrewAssignmentResponse {

  private String id;

  private String scheduleId;

  private String profileId;

  private String accountId;

  private String employeeCardId;

  private String rankOnBoard;

  private Instant boardingTime;

  private Instant disembarkTime;

  private String boardingPort;

  private String disembarkPort;

  private String fullName;

  private String note;
}

package com.inlaco.crewmgrservice.feature.shipschedule.application.model;

import com.inlaco.crewmgrservice.shared.objectvalue.Gender;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShipScheduleAssignedCrewDetail {

  private String profileId;

  private String employeeCardId;

  private String fullName;

  private String rankOnBoard;

  private String boardingPort;

  private String disembarkPort;

  private Instant boardingTime;

  private Instant disembarkTime;

  private String email;

  private String phoneNumber;

  private Gender gender;

  private String address;

  private String note;
}

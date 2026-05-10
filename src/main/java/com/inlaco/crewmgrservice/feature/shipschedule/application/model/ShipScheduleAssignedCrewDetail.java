package com.inlaco.crewmgrservice.feature.shipschedule.application.model;

import com.inlaco.crewmgrservice.shared.objectvalue.Gender;
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

  private String email;

  private String phoneNumber;

  private Gender gender;

  private String address;
}

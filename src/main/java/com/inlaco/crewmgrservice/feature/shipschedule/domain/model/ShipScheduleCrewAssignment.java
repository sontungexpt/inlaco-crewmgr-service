package com.inlaco.crewmgrservice.feature.shipschedule.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShipScheduleCrewAssignment {
  private String id;

  private ObjectId scheduleId;

  private ObjectId profileId;
  private ObjectId accountId;
  private String employeeCardId;

  private String rankOnBoard;

  // This is snapshot information for legitimate crew member
  private String fullName;
  private String phoneNumber;
  private String email;
}

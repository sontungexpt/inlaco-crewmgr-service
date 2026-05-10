package com.inlaco.crewmgrservice.feature.shipschedule.infrastructure.persistence.mongodb.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShipScheduleCrewAssignmentEntity {

  @Id private String id;

  private String shipIMO;
  private ObjectId scheduleId;

  private ObjectId profileId;
  private ObjectId accountId;
  private String employeeCardId;

  private String rankOnBoard;

  // This is snapshot information for legitimate crew member
  private String fullName;
}

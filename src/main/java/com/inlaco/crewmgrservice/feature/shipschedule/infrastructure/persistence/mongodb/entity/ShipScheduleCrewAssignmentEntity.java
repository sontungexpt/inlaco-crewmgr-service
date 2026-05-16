package com.inlaco.crewmgrservice.feature.shipschedule.infrastructure.persistence.mongodb.entity;

import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "ship_schedule_crew_assignments")
public class ShipScheduleCrewAssignmentEntity {

  @Id private String id;

  private String shipIMO;
  private ObjectId scheduleId;

  private ObjectId profileId;
  private ObjectId accountId;
  private String employeeCardId;

  private String rankOnBoard;

  private Instant boardingTime;
  private Instant disembarkTime;

  private String boardingPort;
  private String disembarkPort;

  // This is snapshot information for legitimate crew member
  private String fullName;
  private String note;
}

package com.inlaco.crewmgrservice.feature.crewmobilization.infrastructure.persistence.mongodb.entity;

import java.time.Instant;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@EqualsAndHashCode(of = {"profileId", "accountId", "employeeCardId"})
@Document(collection = "crew_mobilization_assignments")
public class CrewMobilizationAssignmentEntity {

  @Id private String id;

  @Indexed private ObjectId mobilizationId;

  @Indexed private ObjectId profileId;

  @Indexed private ObjectId accountId;

  @Indexed private String employeeCardId;

  private String rankOnBoard;

  @Indexed private Instant startDate;

  @Indexed private Instant endDate;
}

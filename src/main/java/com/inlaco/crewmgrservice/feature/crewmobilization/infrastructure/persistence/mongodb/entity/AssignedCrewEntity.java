package com.inlaco.crewmgrservice.feature.crewmobilization.infrastructure.persistence.mongodb.entity;

import java.time.Instant;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bson.types.ObjectId;

@Data
@EqualsAndHashCode(of = {"profileId", "accountId", "employeeCardId"})
public class AssignedCrewEntity {

  private ObjectId profileId;

  private ObjectId accountId;

  private String employeeCardId;

  private String rankOnBoard;

  private Instant startDate;

  private Instant endDate;
}

package com.inlaco.crewmgrservice.feature.crewmobilization.domain.model;

import java.time.Instant;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class AssignedCrew {

  private String profileId;

  private String accountId;

  private String employeeCardId;

  private String rankOnBoard;

  private Instant startDate;

  private Instant endDate;

  private String remark;
}

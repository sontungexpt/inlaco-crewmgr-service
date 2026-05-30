package com.inlaco.crewmgrservice.feature.crewmobilization.domain.model;

import java.time.Instant;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class CrewMobilizationAssignment {

  private String id;

  private String mobilizationId;

  private String profileId;
  private String accountId;
  private String employeeCardId;

  private String rankOnBoard;
  private String shipIMO;

  private Instant startDate;
  private Instant endDate;

  private String remark;
}

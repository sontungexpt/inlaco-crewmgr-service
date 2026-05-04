package com.inlaco.crewmgrservice.feature.crewmobilization.application.model;

import com.inlaco.crewmgrservice.feature.crewmobilization.domain.enums.CrewMobilizationStatus;
import java.time.Instant;
import lombok.Data;

@Data
public class CrewMobilizationSearchCriteria {

  private String keyword;
  private String accountId;
  private CrewMobilizationStatus status;
  private Instant startDate;
  private Instant endDate;
}

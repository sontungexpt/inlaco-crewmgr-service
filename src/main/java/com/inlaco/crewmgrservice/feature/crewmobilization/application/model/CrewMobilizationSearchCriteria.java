package com.inlaco.crewmgrservice.feature.crewmobilization.application.model;

import com.inlaco.crewmgrservice.feature.crewmobilization.domain.enums.CrewMobilizationStatus;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CrewMobilizationSearchCriteria {

  private String keyword;
  // Cient id of this mobilization
  private String clientId;

  // Crew id of this mobilization
  private String accountId;

  private String shipIMO;
  private CrewMobilizationStatus status;
  private Instant startDate;
  private Instant endDate;
}

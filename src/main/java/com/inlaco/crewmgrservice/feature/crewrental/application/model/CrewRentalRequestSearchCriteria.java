package com.inlaco.crewmgrservice.feature.crewrental.application.model;

import com.inlaco.crewmgrservice.feature.crewrental.domain.enums.CrewRentalRequestStatus;
import lombok.Data;

@Data
public class CrewRentalRequestSearchCriteria {
  private String accountId;
  private String keyword;
  private CrewRentalRequestStatus status;
}

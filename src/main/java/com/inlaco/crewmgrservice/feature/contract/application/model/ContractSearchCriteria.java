package com.inlaco.crewmgrservice.feature.contract.application.model;

import com.inlaco.crewmgrservice.feature.contract.domain.enums.ContractType;
import java.time.Instant;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContractSearchCriteria {
  private Boolean signed;
  private ContractType type;

  private Instant activationDateStart;
  private Instant activationDateEnd;

  private Instant expiredDateStart;
  private Instant expiredDateEnd;
}

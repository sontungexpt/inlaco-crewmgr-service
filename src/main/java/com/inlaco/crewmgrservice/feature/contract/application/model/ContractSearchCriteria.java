package com.inlaco.crewmgrservice.feature.contract.application.model;

import com.inlaco.crewmgrservice.feature.contract.domain.enums.ContractStatus;
import com.inlaco.crewmgrservice.feature.contract.domain.enums.ContractType;
import java.time.Instant;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContractSearchCriteria {
  private String keyword;

  private String relativeAccountId;

  private Boolean signed;
  private Boolean active;

  private Set<ContractStatus> includedStatuses;
  private Set<ContractStatus> excludedStatuses;

  private ContractType type;

  private Instant activationDateStart;
  private Instant activationDateEnd;

  private Instant expiredDateStart;
  private Instant expiredDateEnd;
}

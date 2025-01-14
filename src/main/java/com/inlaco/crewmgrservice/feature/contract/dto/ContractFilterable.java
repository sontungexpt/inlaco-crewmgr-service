package com.inlaco.crewmgrservice.feature.contract.dto;

import com.inlaco.crewmgrservice.common.payload.Filterable;
import com.inlaco.crewmgrservice.feature.contract.model.ContractType;
import java.time.Instant;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ContractFilterable implements Filterable {
  private ContractType type;
  private Instant activationDateStart;
  private Instant activationDateEnd;
  private Instant expiredDateStart;
  private Instant expiredDateEnd;
  private Boolean signed;
}

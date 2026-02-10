package com.inlaco.crewmgrservice.feature.contract.presentation.dto;

import com.inlaco.crewmgrservice.feature.contract.domain.model.ContractType;
import com.inlaco.crewmgrservice.infrastructure.web.payload.request.filter.Filterable;
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

package com.inlaco.crewmgrservice.feature.contract.domain.objectvalue;

import com.inlaco.crewmgrservice.feature.contract.domain.enums.ContractStatus;
import java.time.Instant;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ContractStatusHistory {

  private ContractStatus fromStatus;

  private ContractStatus toStatus;

  private String changedBy;

  private Instant changedAt;

  private String reason;
}

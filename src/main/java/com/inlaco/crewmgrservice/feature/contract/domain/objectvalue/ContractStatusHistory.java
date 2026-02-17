package com.inlaco.crewmgrservice.feature.contract.domain.objectvalue;

import com.inlaco.crewmgrservice.feature.contract.domain.enums.ContractStatus;
import java.time.Instant;

public record ContractStatusHistory(
    ContractStatus fromStatus,
    ContractStatus toStatus,
    String changedBy,
    Instant changedAt,
    String reason) {}

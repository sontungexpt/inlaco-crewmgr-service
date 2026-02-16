package com.inlaco.crewmgrservice.feature.contract.domain.event;

import com.inlaco.crewmgrservice.feature.contract.domain.model.AbstractContract;
import java.time.Instant;
import java.util.List;

public record ContractExpiredEvent(List<AbstractContract> contracts, Instant now) {}

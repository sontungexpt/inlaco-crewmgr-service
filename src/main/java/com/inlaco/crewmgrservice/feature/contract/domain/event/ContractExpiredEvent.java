package com.inlaco.crewmgrservice.feature.contract.domain.event;

import com.inlaco.crewmgrservice.feature.contract.domain.model.Contract;
import java.time.Instant;
import java.util.List;

public record ContractExpiredEvent(List<Contract> contracts, Instant now) {}

package com.inlaco.crewmgrservice.feature.contract.domain.event;

import com.inlaco.crewmgrservice.feature.contract.domain.model.AbstractContract;

public record ContractExpiredEvent(AbstractContract contract) {}

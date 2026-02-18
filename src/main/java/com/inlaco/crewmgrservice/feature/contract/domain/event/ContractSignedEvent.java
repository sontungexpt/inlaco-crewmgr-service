package com.inlaco.crewmgrservice.feature.contract.domain.event;

import com.inlaco.crewmgrservice.feature.contract.domain.model.Contract;

public record ContractSignedEvent(Contract contract) {}

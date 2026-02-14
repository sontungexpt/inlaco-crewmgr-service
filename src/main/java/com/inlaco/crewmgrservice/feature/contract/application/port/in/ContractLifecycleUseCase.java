package com.inlaco.crewmgrservice.feature.contract.application.port.in;

import com.inlaco.crewmgrservice.feature.contract.domain.model.AbstractContract;
import java.time.Instant;
import java.util.List;

public interface ContractLifecycleUseCase {

  void activateDueContracts(List<AbstractContract> contracts, Instant now);

  void expireContracts(List<AbstractContract> contracts, Instant now);
}

package com.inlaco.crewmgrservice.feature.contract.application.port.in;

import com.inlaco.crewmgrservice.feature.contract.domain.model.Contract;
import java.time.Instant;
import java.util.List;

public interface ContractLifecycleUseCase {

  void activateDueContracts(List<Contract> contracts, Instant now);

  void expireContracts(List<Contract> contracts, Instant now);
}

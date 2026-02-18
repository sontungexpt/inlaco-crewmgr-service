package com.inlaco.crewmgrservice.feature.contract.application.port.in;

import com.inlaco.crewmgrservice.feature.contract.domain.model.Contract;
import com.inlaco.crewmgrservice.feature.contract.domain.model.UpdateContractCommand;

public interface UpdateContractUseCase {
  Contract update(String id, UpdateContractCommand patch);
}

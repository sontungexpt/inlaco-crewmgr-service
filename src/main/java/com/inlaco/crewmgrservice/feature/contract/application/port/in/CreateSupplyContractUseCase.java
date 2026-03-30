package com.inlaco.crewmgrservice.feature.contract.application.port.in;

import com.inlaco.crewmgrservice.feature.contract.application.model.CrewSupplyContractAssets;
import com.inlaco.crewmgrservice.feature.contract.domain.model.Contract;
import com.inlaco.crewmgrservice.feature.contract.domain.model.CrewSupplyContract;
import com.inlaco.crewmgrservice.feature.user.domain.model.User;

public interface CreateSupplyContractUseCase {

  Contract create(
      String requestId, CrewSupplyContract contract, CrewSupplyContractAssets assets, User creator);
}

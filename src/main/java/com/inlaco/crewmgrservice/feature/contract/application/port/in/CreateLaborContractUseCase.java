package com.inlaco.crewmgrservice.feature.contract.application.port.in;

import com.inlaco.crewmgrservice.feature.contract.application.model.ContractAssets;
import com.inlaco.crewmgrservice.feature.contract.domain.model.Contract;
import com.inlaco.crewmgrservice.feature.contract.domain.model.LaborContract;
import com.inlaco.crewmgrservice.feature.user.domain.model.User;

public interface CreateLaborContractUseCase {
  Contract create(
      String applicationId, LaborContract contract, ContractAssets assets, User creator);
}

package com.inlaco.crewmgrservice.feature.contract.application.port.in;

import com.inlaco.crewmgrservice.feature.contract.domain.model.AbstractContract;
import com.inlaco.crewmgrservice.feature.contract.domain.model.CrewSupplyContract;
import com.inlaco.crewmgrservice.feature.user.domain.model.User;

public interface CreateSupplyContractUseCase {

  AbstractContract create(
      String requestId,
      CrewSupplyContract contract,
      String contractFileAssetId,
      String shipImageAssetId,
      User creator);
}

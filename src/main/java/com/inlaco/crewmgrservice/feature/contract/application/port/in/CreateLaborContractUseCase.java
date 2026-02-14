package com.inlaco.crewmgrservice.feature.contract.application.port.in;

import com.inlaco.crewmgrservice.feature.contract.domain.model.AbstractContract;
import com.inlaco.crewmgrservice.feature.contract.domain.model.LaborContract;
import com.inlaco.crewmgrservice.feature.user.domain.model.User;

public interface CreateLaborContractUseCase {
  AbstractContract create(
      String applicationId, LaborContract contract, String contractFileAssetId, User creator);
}

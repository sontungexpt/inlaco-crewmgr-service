package com.inlaco.crewmgrservice.feature.contract.application.port.in;

import com.inlaco.crewmgrservice.feature.contract.domain.model.AbstractContract;
import com.inlaco.crewmgrservice.feature.user.domain.model.User;

public interface SignContractUseCase {
  AbstractContract sign(String contractId, User user);
}

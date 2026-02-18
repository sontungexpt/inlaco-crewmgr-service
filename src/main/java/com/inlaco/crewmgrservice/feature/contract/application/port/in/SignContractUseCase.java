package com.inlaco.crewmgrservice.feature.contract.application.port.in;

import com.inlaco.crewmgrservice.feature.contract.domain.model.Contract;
import com.inlaco.crewmgrservice.feature.user.domain.model.User;

public interface SignContractUseCase {
  Contract sign(String contractId, User user);
}

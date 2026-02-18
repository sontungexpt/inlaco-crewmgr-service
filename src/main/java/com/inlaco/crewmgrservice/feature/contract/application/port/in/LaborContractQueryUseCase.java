package com.inlaco.crewmgrservice.feature.contract.application.port.in;

import com.inlaco.crewmgrservice.feature.contract.domain.model.Contract;

public interface LaborContractQueryUseCase {

  Contract getContractByApplicationId(String applicationId);
}

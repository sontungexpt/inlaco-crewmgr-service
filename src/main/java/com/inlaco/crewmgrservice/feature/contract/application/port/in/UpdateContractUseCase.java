package com.inlaco.crewmgrservice.feature.contract.application.port.in;

import com.fasterxml.jackson.databind.JsonNode;
import com.inlaco.crewmgrservice.feature.contract.domain.model.AbstractContract;

public interface UpdateContractUseCase {
  AbstractContract update(String id, JsonNode patch);
}

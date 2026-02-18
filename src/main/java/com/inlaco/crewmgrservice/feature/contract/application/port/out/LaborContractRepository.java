package com.inlaco.crewmgrservice.feature.contract.application.port.out;

import com.inlaco.crewmgrservice.feature.contract.domain.model.Contract;
import java.util.Optional;

public interface LaborContractRepository {

  boolean existsByApplicationId(String applicationId);

  Optional<Contract> findByApplicationId(String applicationId);
}

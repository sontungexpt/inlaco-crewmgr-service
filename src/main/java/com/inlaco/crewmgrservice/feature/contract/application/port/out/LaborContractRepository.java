package com.inlaco.crewmgrservice.feature.contract.application.port.out;

public interface LaborContractRepository {

  boolean existsByApplicationId(String applicationId);
}

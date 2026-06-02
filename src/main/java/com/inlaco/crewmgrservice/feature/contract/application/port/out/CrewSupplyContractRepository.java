package com.inlaco.crewmgrservice.feature.contract.application.port.out;

import com.inlaco.crewmgrservice.feature.contract.domain.model.Contract;
import java.util.List;

public interface CrewSupplyContractRepository {

  List<Contract> findActiveContractsByShipIMO(String shipImoNumber);

  boolean existsActiveContractsByShipIMO(String shipImoNumber);
}

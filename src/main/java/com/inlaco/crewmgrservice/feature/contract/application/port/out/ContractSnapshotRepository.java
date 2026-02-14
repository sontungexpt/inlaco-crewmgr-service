package com.inlaco.crewmgrservice.feature.contract.application.port.out;

import com.inlaco.crewmgrservice.feature.contract.domain.model.AbstractContract;
import java.util.List;
import java.util.Optional;

public interface ContractSnapshotRepository {

  AbstractContract save(AbstractContract contract);

  List<AbstractContract> findByContractId(String contractId);

  Optional<AbstractContract> findByContractIdAndVersion(String contractId, int version);
}

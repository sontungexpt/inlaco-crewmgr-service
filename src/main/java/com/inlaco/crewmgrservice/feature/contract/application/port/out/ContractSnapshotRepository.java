package com.inlaco.crewmgrservice.feature.contract.application.port.out;

import com.inlaco.crewmgrservice.feature.contract.domain.model.Contract;
import java.util.List;
import java.util.Optional;

public interface ContractSnapshotRepository {

  Contract save(Contract contract);

  List<Contract> findByContractId(String contractId);

  Optional<Contract> findByContractIdAndVersion(String contractId, int version);

  List<Contract> findByContractIdAndVersionLessThan(String contractId, int version);
}

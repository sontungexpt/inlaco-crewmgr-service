package com.inlaco.crewmgrservice.feature.contract.application.port.in;

import com.inlaco.crewmgrservice.feature.contract.application.model.ContractSearchCriteria;
import com.inlaco.crewmgrservice.feature.contract.domain.model.Contract;
import com.inlaco.crewmgrservice.feature.user.domain.model.User;
import java.util.List;
import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ContractQueryUseCase {

  Contract getContract(String id, @Nullable Integer version, User user);

  Page<? extends Contract> getContracts(ContractSearchCriteria criteria, Pageable pageable);

  Page<? extends Contract> getContractsByUser(
      ContractSearchCriteria criteria, User user, Pageable pageable);

  List<Contract> getOldContractVersions(String contractId, Integer currentVersion);
}

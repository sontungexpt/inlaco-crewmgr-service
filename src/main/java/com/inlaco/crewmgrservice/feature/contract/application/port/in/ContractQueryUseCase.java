package com.inlaco.crewmgrservice.feature.contract.application.port.in;

import com.inlaco.crewmgrservice.feature.contract.application.model.ContractSearchCriteria;
import com.inlaco.crewmgrservice.feature.contract.domain.model.Contract;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ContractQueryUseCase {

  Contract getContract(String id);

  Page<? extends Contract> getContracts(ContractSearchCriteria criteria, Pageable pageable);
}

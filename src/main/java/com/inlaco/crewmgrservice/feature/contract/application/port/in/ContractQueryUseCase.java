package com.inlaco.crewmgrservice.feature.contract.application.port.in;

import com.inlaco.crewmgrservice.feature.contract.application.model.ContractSearchCriteria;
import com.inlaco.crewmgrservice.feature.contract.domain.model.AbstractContract;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ContractQueryUseCase {

  AbstractContract getContract(String id);

  Page<? extends AbstractContract> getContracts(ContractSearchCriteria criteria, Pageable pageable);
}

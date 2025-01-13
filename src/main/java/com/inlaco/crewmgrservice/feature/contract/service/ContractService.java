package com.inlaco.crewmgrservice.feature.contract.service;

import com.inlaco.crewmgrservice.feature.contract.model.AbstractContract;
import com.inlaco.crewmgrservice.feature.contract.model.Contract;
import com.inlaco.crewmgrservice.feature.contract.model.ContractVersion;

public interface ContractService {

  Contract getContractById(String id);

  Contract getSailorContract(String sailorId);

  Contract addContract(AbstractContract contract);

  Contract saveContract(AbstractContract contract);

  Contract createLaborContract(String sailorId, AbstractContract contract);

  ContractVersion saveVersion(ContractVersion contract);
}

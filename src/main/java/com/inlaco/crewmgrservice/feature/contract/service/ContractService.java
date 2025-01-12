package com.inlaco.crewmgrservice.feature.contract.service;

import com.inlaco.crewmgrservice.feature.contract.model.Contract;

public interface ContractService {

  Contract getContractById(String id);

  Contract getContractBySailorId(String sailorId);

  Contract updateContract(String contractId);
}

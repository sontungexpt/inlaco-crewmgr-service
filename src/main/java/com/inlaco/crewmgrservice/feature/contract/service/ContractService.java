package com.inlaco.crewmgrservice.feature.contract.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.inlaco.crewmgrservice.feature.contract.dto.ContractFilterRequest;
import com.inlaco.crewmgrservice.feature.contract.model.AbstractContract;
import com.inlaco.crewmgrservice.feature.contract.model.Contract;
import com.inlaco.crewmgrservice.feature.contract.model.ContractVersion;
import com.inlaco.crewmgrservice.feature.user.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ContractService {

  AbstractContract getContractById(String id);

  Page<? extends Contract> getSailorContracts(String sailorId, Pageable pageable);

  Page<? extends Contract> getAllContracts(ContractFilterRequest filterRequest, Pageable pageable);

  Contract updateContract(String id, JsonNode patch);

  Contract addContract(AbstractContract contract);

  Contract saveContract(AbstractContract contract);

  Contract createSailorLaborContract(String sailorId, AbstractContract contract, User creator);

  ContractVersion getContractVersionById(String id);

  ContractVersion saveVersion(ContractVersion contract);
}

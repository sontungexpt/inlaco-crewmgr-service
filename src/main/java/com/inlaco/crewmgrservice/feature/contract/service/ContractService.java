package com.inlaco.crewmgrservice.feature.contract.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.inlaco.crewmgrservice.feature.contract.dto.ContractFilterable;
import com.inlaco.crewmgrservice.feature.contract.model.AbstractContract;
import com.inlaco.crewmgrservice.feature.contract.model.Contract;
import com.inlaco.crewmgrservice.feature.contract.model.ContractVersion;
import com.inlaco.crewmgrservice.feature.contract.model.LaborContract;
import com.inlaco.crewmgrservice.feature.contract.model.SupplyContract;
import com.inlaco.crewmgrservice.feature.user.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ContractService {

  AbstractContract getContractById(String id);

  Page<? extends Contract> getAllContracts(ContractFilterable filterRequest, Pageable pageable);

  Contract getLaborContractByEmployeeId(String employeeId);

  Contract updateContract(String id, JsonNode patch, boolean newVersion);

  Contract saveContract(AbstractContract contract);

  Contract createLaborContract(
      String sailorId, LaborContract contract, String contractFilePubId, User creator);

  Contract createSupplyContract(String crewRentalRequestId, SupplyContract contract, User creator);

  Contract activeContract(String contractId, User activer);

  ContractVersion getContractVersionById(String id);

  ContractVersion saveVersion(ContractVersion contract);
}

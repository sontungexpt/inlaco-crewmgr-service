package com.inlaco.crewmgrservice.feature.contract.application.port.in;

import com.fasterxml.jackson.databind.JsonNode;
import com.inlaco.crewmgrservice.feature.contract.domain.model.AbstractContract;
import com.inlaco.crewmgrservice.feature.contract.domain.model.Contract;
import com.inlaco.crewmgrservice.feature.contract.domain.model.ContractVersion;
import com.inlaco.crewmgrservice.feature.contract.domain.model.LaborContract;
import com.inlaco.crewmgrservice.feature.contract.domain.model.SupplyContract;
import com.inlaco.crewmgrservice.feature.contract.presentation.dto.ContractFilterable;
import com.inlaco.crewmgrservice.feature.user.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ContractUseCase {

  AbstractContract getContractById(String id);

  Page<? extends Contract> getAllContracts(ContractFilterable filterRequest, Pageable pageable);

  Contract getLaborContractByEmployeeId(String employeeId);

  Contract updateContract(String id, JsonNode patch, boolean newVersion);

  Contract saveContract(AbstractContract contract);

  Contract createLaborContract(
      String sailorId, LaborContract contract, String contractFileAssetId, User creator);

  Contract createSupplyContract(
      String crewRentalRequestId,
      SupplyContract contract,
      String contractFileAssetId,
      String shipImageAssetId,
      User creator);

  Contract signContract(String contractId, User activer);

  ContractVersion getContractVersionById(String id);

  ContractVersion saveVersion(ContractVersion contract);
}

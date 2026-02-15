package com.inlaco.crewmgrservice.feature.contract.presentation.mapper;

import com.inlaco.crewmgrservice.feature.contract.domain.model.AbstractContract;
import com.inlaco.crewmgrservice.feature.contract.domain.model.CrewSupplyContract;
import com.inlaco.crewmgrservice.feature.contract.domain.model.LaborContract;
import com.inlaco.crewmgrservice.feature.contract.presentation.dto.request.AbstractContractRequest;
import com.inlaco.crewmgrservice.feature.contract.presentation.dto.request.CrewSupplyContractRequest;
import com.inlaco.crewmgrservice.feature.contract.presentation.dto.request.LaborContractRequest;
import com.inlaco.crewmgrservice.feature.contract.presentation.dto.response.AbstractContractResponse;
import com.inlaco.crewmgrservice.feature.contract.presentation.dto.response.CrewSupplyContractResponse;
import com.inlaco.crewmgrservice.feature.contract.presentation.dto.response.LaborContractResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ContractMapper {

  default AbstractContractResponse toContractResponse(AbstractContract abstractContract) {
    return switch (abstractContract) {
      case LaborContract contract -> toLaborContractResponse(contract);
      case CrewSupplyContract contract -> toCrewSupplyContractResponse(contract);
      default -> throw new IllegalArgumentException("Unsupported contract type");
    };
  }

  LaborContractResponse toLaborContractResponse(LaborContract contract);

  CrewSupplyContractResponse toCrewSupplyContractResponse(CrewSupplyContract contract);

  default AbstractContract toContract(AbstractContractRequest request) {
    return switch (request) {
      case LaborContractRequest contract -> toLaborContract(contract);
      case CrewSupplyContractRequest contract -> toCrewSupplyContract(contract);
      default -> throw new IllegalArgumentException("Unsupported contract type");
    };
  }

  LaborContract toLaborContract(LaborContractRequest request);

  CrewSupplyContract toCrewSupplyContract(CrewSupplyContractRequest request);
}

package com.inlaco.crewmgrservice.feature.contract.presentation.mapper;

import com.inlaco.crewmgrservice.feature.contract.domain.model.AbstractContract;
import com.inlaco.crewmgrservice.feature.contract.domain.model.CrewSupplyContract;
import com.inlaco.crewmgrservice.feature.contract.domain.model.DynamicContract;
import com.inlaco.crewmgrservice.feature.contract.domain.model.LaborContract;
import com.inlaco.crewmgrservice.feature.contract.presentation.dto.request.AbstractContractRequest;
import com.inlaco.crewmgrservice.feature.contract.presentation.dto.request.CrewSupplyContractRequest;
import com.inlaco.crewmgrservice.feature.contract.presentation.dto.request.DynamicContractRequest;
import com.inlaco.crewmgrservice.feature.contract.presentation.dto.request.LaborContractRequest;
import com.inlaco.crewmgrservice.feature.contract.presentation.dto.response.AbstractContractResponse;
import com.inlaco.crewmgrservice.feature.contract.presentation.dto.response.CrewSupplyContractResponse;
import com.inlaco.crewmgrservice.feature.contract.presentation.dto.response.DynamicContractResponse;
import com.inlaco.crewmgrservice.feature.contract.presentation.dto.response.LaborContractResponse;
import org.mapstruct.Mapper;
import org.mapstruct.SubclassExhaustiveStrategy;
import org.mapstruct.SubclassMapping;

@Mapper(
    componentModel = "spring",
    subclassExhaustiveStrategy = SubclassExhaustiveStrategy.RUNTIME_EXCEPTION)
public interface ContractMapper {

  @SubclassMapping(source = LaborContractRequest.class, target = LaborContract.class)
  @SubclassMapping(source = CrewSupplyContractRequest.class, target = CrewSupplyContract.class)
  @SubclassMapping(source = DynamicContractRequest.class, target = DynamicContract.class)
  AbstractContract toContract(AbstractContractRequest request);

  LaborContract toLaborContract(LaborContractRequest request);

  CrewSupplyContract toCrewSupplyContract(CrewSupplyContractRequest request);

  DynamicContract toDynamicContract(DynamicContractRequest request);

  @SubclassMapping(source = LaborContract.class, target = LaborContractResponse.class)
  @SubclassMapping(source = CrewSupplyContract.class, target = CrewSupplyContractResponse.class)
  @SubclassMapping(source = DynamicContract.class, target = DynamicContractResponse.class)
  AbstractContractResponse toContractResponse(AbstractContract contract);

  LaborContractResponse toLaborContractResponse(LaborContract contract);

  DynamicContractResponse toDynamicContractResponse(DynamicContract contract);

  CrewSupplyContractResponse toCrewSupplyContractResponse(CrewSupplyContract contract);
}

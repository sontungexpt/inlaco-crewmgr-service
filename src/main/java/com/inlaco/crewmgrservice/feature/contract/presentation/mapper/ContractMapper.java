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
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.SubclassExhaustiveStrategy;
import org.mapstruct.SubclassMapping;

@Mapper(
    componentModel = "spring",
    subclassExhaustiveStrategy = SubclassExhaustiveStrategy.RUNTIME_EXCEPTION)
public interface ContractMapper {

  @SubclassMapping(source = LaborContract.class, target = LaborContractResponse.class)
  @SubclassMapping(source = CrewSupplyContract.class, target = CrewSupplyContractResponse.class)
  AbstractContractResponse toContractResponse(AbstractContract contract);

  LaborContractResponse toLaborContractResponse(LaborContract contract);

  CrewSupplyContractResponse toCrewSupplyContractResponse(CrewSupplyContract contract);

  @SubclassMapping(source = LaborContractRequest.class, target = LaborContract.class)
  @SubclassMapping(source = CrewSupplyContractRequest.class, target = CrewSupplyContract.class)
  @Mapping(target = "contractFile", ignore = true)
  @Mapping(target = "attachments", ignore = true)
  AbstractContract toContract(AbstractContractRequest request);

  @InheritConfiguration(name = "toContract")
  LaborContract toLaborContract(LaborContractRequest request);

  @InheritConfiguration(name = "toContract")
  CrewSupplyContract toCrewSupplyContract(CrewSupplyContractRequest request);
}

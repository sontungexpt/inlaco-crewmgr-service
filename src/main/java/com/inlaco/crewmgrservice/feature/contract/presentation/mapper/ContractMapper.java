package com.inlaco.crewmgrservice.feature.contract.presentation.mapper;

import com.inlaco.crewmgrservice.feature.contract.domain.model.Contract;
import com.inlaco.crewmgrservice.feature.contract.domain.model.CrewSupplyContract;
import com.inlaco.crewmgrservice.feature.contract.domain.model.LaborContract;
import com.inlaco.crewmgrservice.feature.contract.domain.model.UpdateContractCommand;
import com.inlaco.crewmgrservice.feature.contract.domain.model.party.LaborParty;
import com.inlaco.crewmgrservice.feature.contract.domain.model.party.Party;
import com.inlaco.crewmgrservice.feature.contract.domain.objectvalue.Version;
import com.inlaco.crewmgrservice.feature.contract.presentation.dto.party.LaborPartyDTO;
import com.inlaco.crewmgrservice.feature.contract.presentation.dto.party.PartyDTO;
import com.inlaco.crewmgrservice.feature.contract.presentation.dto.request.create.NewContract;
import com.inlaco.crewmgrservice.feature.contract.presentation.dto.request.create.NewCrewSupplyContract;
import com.inlaco.crewmgrservice.feature.contract.presentation.dto.request.create.NewLaborContract;
import com.inlaco.crewmgrservice.feature.contract.presentation.dto.request.update.UpdateContractRequest;
import com.inlaco.crewmgrservice.feature.contract.presentation.dto.response.ContractResponse;
import com.inlaco.crewmgrservice.feature.contract.presentation.dto.response.CrewSupplyContractResponse;
import com.inlaco.crewmgrservice.feature.contract.presentation.dto.response.LaborContractResponse;
import com.inlaco.crewmgrservice.shared.mapstruct.mapper.FieldUpdateMapper;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.SubclassExhaustiveStrategy;
import org.mapstruct.SubclassMapping;

@Mapper(
    componentModel = "spring",
    unmappedSourcePolicy = ReportingPolicy.IGNORE,
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    uses = {FieldUpdateMapper.class},
    subclassExhaustiveStrategy = SubclassExhaustiveStrategy.RUNTIME_EXCEPTION)
public interface ContractMapper {

  // contract to response
  @SubclassMapping(source = LaborContract.class, target = LaborContractResponse.class)
  @SubclassMapping(source = CrewSupplyContract.class, target = CrewSupplyContractResponse.class)
  ContractResponse toContractResponse(Contract contract);

  LaborContractResponse toLaborContractResponse(LaborContract contract);

  CrewSupplyContractResponse toCrewSupplyContractResponse(CrewSupplyContract contract);

  // request to contract
  @SubclassMapping(source = NewLaborContract.class, target = LaborContract.class)
  @SubclassMapping(source = NewCrewSupplyContract.class, target = CrewSupplyContract.class)
  @Mapping(target = "contractFile", ignore = true)
  @Mapping(target = "attachments", ignore = true)
  Contract toContract(NewContract request);

  @InheritConfiguration(name = "toContract")
  LaborContract toLaborContract(NewLaborContract request);

  @InheritConfiguration(name = "toContract")
  CrewSupplyContract toCrewSupplyContract(NewCrewSupplyContract request);

  // update request to command
  UpdateContractCommand toUpdateContractCommand(UpdateContractRequest request);

  @SubclassMapping(source = LaborPartyDTO.class, target = LaborParty.class)
  Party toParty(PartyDTO partyRequest);

  @InheritConfiguration(name = "toParty")
  LaborParty toLaborParty(PartyDTO partyRequest);

  @SubclassMapping(source = LaborParty.class, target = LaborPartyDTO.class)
  PartyDTO toPartyRequest(Party party);

  @InheritConfiguration(name = "toPartyRequest")
  LaborPartyDTO toLaborPartyRequest(PartyDTO partyRequest);

  default int map(Version version) {
    return version.num();
  }
}

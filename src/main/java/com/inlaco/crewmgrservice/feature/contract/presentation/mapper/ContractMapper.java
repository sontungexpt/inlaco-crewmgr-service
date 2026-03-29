package com.inlaco.crewmgrservice.feature.contract.presentation.mapper;

import com.inlaco.crewmgrservice.feature.contract.domain.model.Contract;
import com.inlaco.crewmgrservice.feature.contract.domain.model.CrewSupplyContract;
import com.inlaco.crewmgrservice.feature.contract.domain.model.LaborContract;
import com.inlaco.crewmgrservice.feature.contract.domain.model.UpdateContractCommand;
import com.inlaco.crewmgrservice.feature.contract.domain.model.UpdateLaborContractCommand;
import com.inlaco.crewmgrservice.feature.contract.domain.model.party.LaborParty;
import com.inlaco.crewmgrservice.feature.contract.domain.model.party.Party;
import com.inlaco.crewmgrservice.feature.contract.domain.objectvalue.Version;
import com.inlaco.crewmgrservice.feature.contract.presentation.dto.party.LaborPartyDTO;
import com.inlaco.crewmgrservice.feature.contract.presentation.dto.party.PartyDTO;
import com.inlaco.crewmgrservice.feature.contract.presentation.dto.request.create.NewContract;
import com.inlaco.crewmgrservice.feature.contract.presentation.dto.request.create.NewCrewSupplyContract;
import com.inlaco.crewmgrservice.feature.contract.presentation.dto.request.create.NewLaborContract;
import com.inlaco.crewmgrservice.feature.contract.presentation.dto.request.update.ContractPatchRequest;
import com.inlaco.crewmgrservice.feature.contract.presentation.dto.request.update.LaborContractPatchRequest;
import com.inlaco.crewmgrservice.feature.contract.presentation.dto.response.ContractResponse;
import com.inlaco.crewmgrservice.feature.contract.presentation.dto.response.CrewSupplyContractResponse;
import com.inlaco.crewmgrservice.feature.contract.presentation.dto.response.LaborContractResponse;
import com.inlaco.crewmgrservice.shared.application.model.Patch;
import java.util.List;
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
    subclassExhaustiveStrategy = SubclassExhaustiveStrategy.RUNTIME_EXCEPTION)
public abstract class ContractMapper {

  // contract to response
  @SubclassMapping(source = LaborContract.class, target = LaborContractResponse.class)
  @SubclassMapping(source = CrewSupplyContract.class, target = CrewSupplyContractResponse.class)
  public abstract ContractResponse toContractResponse(Contract contract);

  // request to contract
  @SubclassMapping(source = NewLaborContract.class, target = LaborContract.class)
  @SubclassMapping(source = NewCrewSupplyContract.class, target = CrewSupplyContract.class)
  @Mapping(target = "contractFile", ignore = true)
  @Mapping(target = "attachments", ignore = true)
  public abstract Contract toContract(NewContract request);

  @InheritConfiguration(name = "toContract")
  public abstract LaborContract toLaborContract(NewLaborContract request);

  @InheritConfiguration(name = "toContract")
  public abstract CrewSupplyContract toCrewSupplyContract(NewCrewSupplyContract request);

  // update request to command
  @SubclassMapping(
      source = LaborContractPatchRequest.class,
      target = UpdateLaborContractCommand.class)
  public abstract UpdateContractCommand toUpdateContractCommand(ContractPatchRequest request);

  @SubclassMapping(source = LaborPartyDTO.class, target = LaborParty.class)
  public abstract Party toParty(PartyDTO partyRequest);

  @SubclassMapping(source = LaborParty.class, target = LaborPartyDTO.class)
  public abstract PartyDTO toPartyDTO(Party party);

  int map(Version version) {
    return version.num();
  }

  Patch<Party> map(Patch<PartyDTO> patch) {
    return patch.map(this::toParty);
  }

  Patch<List<Party>> mapPartners(Patch<List<PartyDTO>> patch) {
    return patch.map(parties -> parties.stream().map(this::toParty).toList());
  }

  // @Named("mapContractFile")
  // Asset mapContractFile(String contractFileId) {
  //   return uploadDispatcher.fetch(AssetType.CONTRACT_FILE, contractFileId);
  // }
}

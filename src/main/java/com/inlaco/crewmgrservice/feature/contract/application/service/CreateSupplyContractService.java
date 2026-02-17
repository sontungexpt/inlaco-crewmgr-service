package com.inlaco.crewmgrservice.feature.contract.application.service;

import com.inlaco.crewmgrservice.feature.contract.application.port.in.CreateSupplyContractUseCase;
import com.inlaco.crewmgrservice.feature.contract.application.port.out.ContractRepository;
import com.inlaco.crewmgrservice.feature.contract.domain.model.AbstractContract;
import com.inlaco.crewmgrservice.feature.contract.domain.model.CrewSupplyContract;
import com.inlaco.crewmgrservice.feature.crewrental.application.port.in.CrewRentalRequestCommandUseCase;
import com.inlaco.crewmgrservice.feature.crewrental.application.port.in.CrewRentalRequestQueryUseCase;
import com.inlaco.crewmgrservice.feature.upload.application.port.in.UploadDispatcher;
import com.inlaco.crewmgrservice.feature.upload.domain.enums.AssetType;
import com.inlaco.crewmgrservice.feature.user.domain.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreateSupplyContractService implements CreateSupplyContractUseCase {

  private final ContractRepository contractRepository;
  private final CrewRentalRequestCommandUseCase crewRentalRequestCommandUseCase;
  private final CrewRentalRequestQueryUseCase crewRentalRequestQueryUseCase;
  private final UploadDispatcher uploadDispatcher;

  @Override
  @Transactional
  public AbstractContract create(
      String requestId,
      CrewSupplyContract contract,
      String contractFileAssetId,
      String shipImageAssetId,
      User creator) {
    var crewRentalRequest = crewRentalRequestQueryUseCase.getRequest(requestId);

    contract.setCrewRentalRequestId(requestId);
    contract.setContractFile(uploadDispatcher.fetch(AssetType.CONTRACT_FILE, contractFileAssetId));
    contract.getShipInfo().setImage(uploadDispatcher.fetch(AssetType.SHIP_IMAGE, shipImageAssetId));

    var newContract = contractRepository.save(contract);
    crewRentalRequestCommandUseCase.markSigning(crewRentalRequest, newContract.getId());

    log.debug("Created supply contract for crew rental request with id: {}", requestId);

    return newContract;
  }
}

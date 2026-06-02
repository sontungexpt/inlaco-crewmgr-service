package com.inlaco.crewmgrservice.feature.contract.application.service;

import com.inlaco.crewmgrservice.feature.contract.application.model.CrewSupplyContractAssets;
import com.inlaco.crewmgrservice.feature.contract.application.port.in.CreateSupplyContractUseCase;
import com.inlaco.crewmgrservice.feature.contract.application.port.out.ContractRepository;
import com.inlaco.crewmgrservice.feature.contract.domain.model.Contract;
import com.inlaco.crewmgrservice.feature.contract.domain.model.CrewSupplyContract;
import com.inlaco.crewmgrservice.feature.crewrental.application.port.in.CrewRentalRequestCommandUseCase;
import com.inlaco.crewmgrservice.feature.crewrental.application.port.in.CrewRentalRequestQueryUseCase;
import com.inlaco.crewmgrservice.feature.upload.application.port.in.UploadDispatcher;
import com.inlaco.crewmgrservice.feature.upload.domain.enums.AssetType;
import com.inlaco.crewmgrservice.feature.user.domain.model.User;
import java.util.List;
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
  public Contract create(
      String requestId,
      CrewSupplyContract contract,
      CrewSupplyContractAssets assets,
      User creator) {
    var crewRentalRequest = crewRentalRequestQueryUseCase.getRequest(requestId);

    contract.setCrewRentalRequestId(requestId);
    contract.getPartners().get(0).setAccountId(crewRentalRequest.getAccountId());
    contract.setAccountId(crewRentalRequest.getAccountId());

    String contractFileAssetId = assets.getContractFile();
    if (contractFileAssetId != null) {
      log.debug("Fetching contract file asset with ID: {}", contractFileAssetId);
      contract.setContractFile(
          uploadDispatcher.fetch(AssetType.CONTRACT_FILE, contractFileAssetId));
    }

    String shipImageAssetId = assets.getShipInfoImage();
    if (shipImageAssetId != null) {
      log.debug("Fetching ship image asset with ID: {}", shipImageAssetId);
      contract
          .getShipInfo()
          .setImage(uploadDispatcher.fetch(AssetType.SHIP_IMAGE, shipImageAssetId));
    }

    List<String> attachments = assets.getAttachments();
    if (attachments != null && !attachments.isEmpty()) {
      log.debug("Fetching {} attachment(s) for the contract", attachments.size());
      contract.setAttachments(
          attachments.stream()
              .map(
                  attachment -> {
                    log.debug("Fetching attachment with ID: {}", attachment);
                    return uploadDispatcher.fetch(AssetType.CONTRACT_FILE, attachment);
                  })
              .toList());
    }

    var newContract = contractRepository.save(contract);
    crewRentalRequestCommandUseCase.markSigning(crewRentalRequest, newContract.getId());

    log.info("Successfully created supply contract for crew rental request with id: {}", requestId);

    return newContract;
  }
}

package com.inlaco.crewmgrservice.feature.contract.application.service;

import com.inlaco.crewmgrservice.feature.contract.application.port.in.UpdateContractUseCase;
import com.inlaco.crewmgrservice.feature.contract.application.port.out.ContractRepository;
import com.inlaco.crewmgrservice.feature.contract.application.port.out.ContractSnapshotRepository;
import com.inlaco.crewmgrservice.feature.contract.domain.model.Contract;
import com.inlaco.crewmgrservice.feature.contract.domain.model.CrewSupplyContract;
import com.inlaco.crewmgrservice.feature.contract.domain.model.LaborContract;
import com.inlaco.crewmgrservice.feature.contract.domain.model.UpdateContractCommand;
import com.inlaco.crewmgrservice.feature.contract.domain.model.UpdateCrewSupplyContractCommand;
import com.inlaco.crewmgrservice.feature.contract.domain.model.UpdateLaborContractCommand;
import com.inlaco.crewmgrservice.feature.contract.domain.model.party.Party;
import com.inlaco.crewmgrservice.feature.upload.application.port.in.UploadDispatcher;
import com.inlaco.crewmgrservice.feature.upload.domain.enums.AssetType;
import com.inlaco.crewmgrservice.shared.kernel.exception.ResourceNotFoundException;
import com.inlaco.crewmgrservice.shared.objectvalue.Asset;
import com.inlaco.crewmgrservice.shared.objectvalue.ShipInfo;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UpdateContractService implements UpdateContractUseCase {

  private final ContractRepository contractRepository;
  private final ContractSnapshotRepository contractSnapshotRepository;
  private final UploadDispatcher uploadDispatcher;

  @Override
  @Transactional
  public Contract update(String id, UpdateContractCommand patch) {

    Contract current =
        contractRepository
            .findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(Contract.class, "id", id));

    Instant now = Instant.now();

    boolean freezed = current.isFreezed(now);

    if (freezed) {
      contractSnapshotRepository.save(current);
      log.debug("Saved contract snapshot with id: {}", id);

    } else {
      cleanupAssets(current, patch);
    }

    boolean changed = false;

    changed |= applyCommonChanges(current, patch);

    changed |= applyCommonAssetChanges(current, patch);

    if (current instanceof LaborContract laborContract
        && patch instanceof UpdateLaborContractCommand laborPatch) {

      changed |= applyLaborChanges(laborContract, laborPatch);
    }

    if (current instanceof CrewSupplyContract crewSupplyContract
        && patch instanceof UpdateCrewSupplyContractCommand supplyPatch) {

      changed |= applyCrewSupplyChanges(crewSupplyContract, supplyPatch);

      changed |= applyCrewSupplyAssetChanges(crewSupplyContract, supplyPatch);
    }

    current.validateDate();

    if (changed && freezed) {
      log.debug("Restarting lifecycle for contract with id: {}", id);
      current.restartLifecycle(now);

      log.debug("Incrementing version for contract with id: {}", id);
      current.incrementVersion(now);
    }

    log.debug("Updated contract with id: {}", id);

    return contractRepository.save(current);
  }

  private boolean applyCommonChanges(Contract contract, UpdateContractCommand patch) {
    return patch.getTitle().ifUpdated(contract::setTitle)
        | patch.getActivationDate().ifUpdated(contract::setActivationDate)
        | patch.getExpiredDate().ifUpdated(contract::setExpiredDate)
        | patch.getContractFreezeDelayMinutes().ifUpdated(contract::setContractFreezeDelayMinutes)
        | patch.getInitiator().ifUpdated(contract::setInitiator)
        | patch
            .getPartners()
            .ifUpdated(
                newPartners -> {
                  for (int index = 0; index < newPartners.size(); index++) {
                    Party newPartner = newPartners.get(index);
                    Party existingPartner = contract.getPartners().get(index);

                    // Fix tạm thời để accountId ko bị mất khi update
                    String accountId = existingPartner.getAccountId();
                    newPartner.setAccountId(accountId);
                  }
                  contract.setPartners(newPartners);
                });
  }

  private boolean applyCommonAssetChanges(Contract contract, UpdateContractCommand patch) {

    boolean changed = false;

    String contractFileId = patch.getContractFile();

    if (contractFileId == null || contractFileId.isBlank()) {

      changed |= contract.getContractFile() != null;

      contract.setContractFile(null);

    } else {

      Asset contractFile = uploadDispatcher.fetch(AssetType.CONTRACT_FILE, contractFileId);

      changed |= contract.getContractFile() != contractFile;

      contract.setContractFile(contractFile);
    }

    List<String> attachmentIds = patch.getAttachments();

    if (attachmentIds == null || attachmentIds.isEmpty()) {

      changed |= contract.getAttachments() != null;

      contract.setAttachments(null);

    } else {

      List<Asset> attachments =
          attachmentIds.stream()
              .map(it -> uploadDispatcher.fetch(AssetType.CONTRACT_FILE, it))
              .toList();

      changed |= contract.getAttachments() != attachments;

      contract.setAttachments(attachments);
    }

    return changed;
  }

  private boolean applyLaborChanges(LaborContract contract, UpdateLaborContractCommand patch) {

    return patch.getPosition().ifUpdated(contract::setPosition)
        | patch.getWorkingLocation().ifUpdated(contract::setWorkingLocation)
        | patch.getBasicSalary().ifUpdated(contract::setBasicSalary)
        | patch.getAllowance().ifUpdated(contract::setAllowance)
        | patch.getReceiveMethod().ifUpdated(contract::setReceiveMethod)
        | patch.getPayday().ifUpdated(contract::setPayday)
        | patch.getSalaryReviewPeriod().ifUpdated(contract::setSalaryReviewPeriod);
  }

  private boolean applyCrewSupplyChanges(
      CrewSupplyContract contract, UpdateCrewSupplyContractCommand patch) {

    AtomicBoolean changed = new AtomicBoolean(false);

    patch
        .getShipInfo()
        .ifUpdated(
            shipInfoPatch -> {
              ShipInfo shipInfo = contract.getShipInfo();

              if (shipInfo == null) {
                shipInfo = new ShipInfo();

                contract.setShipInfo(shipInfo);
              }

              changed.set(
                  changed.get()
                      | shipInfoPatch.getName().ifUpdated(shipInfo::setName)
                      | shipInfoPatch.getType().ifUpdated(shipInfo::setType)
                      | shipInfoPatch.getCountryISO().ifUpdated(shipInfo::setCountryISO)
                      | shipInfoPatch.getImoNumber().ifUpdated(shipInfo::setImoNumber)
                      | shipInfoPatch.getDescription().ifUpdated(shipInfo::setDescription));
            });

    return changed.get();
  }

  private boolean applyCrewSupplyAssetChanges(
      CrewSupplyContract contract, UpdateCrewSupplyContractCommand patch) {
    AtomicBoolean changed = new AtomicBoolean(false);

    patch
        .getShipInfo()
        .ifUpdated(
            shipInfoPatch -> {
              if (contract.getShipInfo() == null) {
                contract.setShipInfo(new ShipInfo());
              }

              shipInfoPatch
                  .getImage()
                  .ifUpdated(
                      imageId -> {
                        Asset image = uploadDispatcher.fetch(AssetType.SHIP_IMAGE, imageId);
                        contract.getShipInfo().setImage(image);
                        changed.set(true);
                      });
            });

    return changed.get();
  }

  private void cleanupAssets(Contract current, UpdateContractCommand patch) {

    Asset currentContractFile = current.getContractFile();

    if (currentContractFile != null) {
      uploadDispatcher.delete(AssetType.CONTRACT_FILE, currentContractFile.assetId());

      log.debug("Deleted contract file {}", currentContractFile.assetId());
    }

    List<Asset> currentAttachments = current.getAttachments();

    if (currentAttachments != null && !currentAttachments.isEmpty()) {

      uploadDispatcher.delete(
          AssetType.CONTRACT_FILE, currentAttachments.stream().map(Asset::assetId).toList());

      log.debug(
          "Deleted contract attachments {}",
          currentAttachments.stream().map(Asset::assetId).toList());
    }

    if (current instanceof CrewSupplyContract crewSupplyContract
        && patch instanceof UpdateCrewSupplyContractCommand supplyPatch) {

      supplyPatch
          .getShipInfo()
          .ifUpdated(
              shipInfoPatch ->
                  shipInfoPatch
                      .getImage()
                      .ifUpdated(
                          imageId -> {
                            ShipInfo shipInfo = crewSupplyContract.getShipInfo();

                            if (shipInfo == null || shipInfo.getImage() == null) {
                              return;
                            }

                            uploadDispatcher.delete(
                                AssetType.SHIP_IMAGE, shipInfo.getImage().assetId());

                            log.debug("Deleted ship image {}", shipInfo.getImage().assetId());
                          }));
    }
  }
}

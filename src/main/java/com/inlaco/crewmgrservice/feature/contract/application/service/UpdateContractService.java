package com.inlaco.crewmgrservice.feature.contract.application.service;

import com.inlaco.crewmgrservice.feature.contract.application.port.in.UpdateContractUseCase;
import com.inlaco.crewmgrservice.feature.contract.application.port.out.ContractRepository;
import com.inlaco.crewmgrservice.feature.contract.application.port.out.ContractSnapshotRepository;
import com.inlaco.crewmgrservice.feature.contract.domain.model.Contract;
import com.inlaco.crewmgrservice.feature.contract.domain.model.UpdateContractCommand;
import com.inlaco.crewmgrservice.feature.upload.application.port.in.UploadDispatcher;
import com.inlaco.crewmgrservice.feature.upload.domain.enums.AssetType;
import com.inlaco.crewmgrservice.shared.kernel.exception.ResourceNotFoundException;
import com.inlaco.crewmgrservice.shared.objectvalue.Asset;
import java.time.Instant;
import java.util.List;
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

    if (current.isFreezed(now)) {
      // Freezed so save snapshot
      contractSnapshotRepository.save(current);
      log.debug("Saved contract snapshot with id: {}", id);
    } else {
      // Not freezed so delete old files before updating
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
    }
    current.amend(
        patch,
        now,
        (contractFileId) -> uploadDispatcher.fetch(AssetType.CONTRACT_FILE, contractFileId),
        (attachmentIds) ->
            attachmentIds.stream()
                .map(attachmentId -> uploadDispatcher.fetch(AssetType.CONTRACT_FILE, attachmentId))
                .toList());

    log.debug("Updated contract with id: {}", id);

    return contractRepository.save(current);
  }
}

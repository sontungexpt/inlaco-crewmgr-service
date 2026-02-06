package com.inlaco.crewmgrservice.feature.contract.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.inlaco.crewmgrservice.application.exception.ResourceAlreadyInUseException;
import com.inlaco.crewmgrservice.application.exception.ResourceNotFoundException;
import com.inlaco.crewmgrservice.feature.contract.dto.ContractFilterable;
import com.inlaco.crewmgrservice.feature.contract.dto.ShortContract;
import com.inlaco.crewmgrservice.feature.contract.exception.FreezeContractUpdateException;
import com.inlaco.crewmgrservice.feature.contract.model.AbstractContract;
import com.inlaco.crewmgrservice.feature.contract.model.Contract;
import com.inlaco.crewmgrservice.feature.contract.model.ContractVersion;
import com.inlaco.crewmgrservice.feature.contract.model.LaborContract;
import com.inlaco.crewmgrservice.feature.contract.model.SupplyContract;
import com.inlaco.crewmgrservice.feature.contract.repository.ContractRepository;
import com.inlaco.crewmgrservice.feature.contract.repository.ContractVersionRepository;
import com.inlaco.crewmgrservice.feature.contract.repository.CustomContractRepository;
import com.inlaco.crewmgrservice.feature.contract.repository.CustomLaborContractRepository;
import com.inlaco.crewmgrservice.feature.contract.service.ContractService;
import com.inlaco.crewmgrservice.feature.crewrental.enums.RentalRequestStatus;
import com.inlaco.crewmgrservice.feature.crewrental.service.RentalRequestService;
import com.inlaco.crewmgrservice.feature.upload.enums.UploadStrategy;
import com.inlaco.crewmgrservice.feature.upload.service.UploadFactory;
import com.inlaco.crewmgrservice.feature.user.model.CandidateProfile;
import com.inlaco.crewmgrservice.feature.user.model.User;
import com.inlaco.crewmgrservice.feature.user.service.CandidateService;
import com.inlaco.crewmgrservice.feature.user.service.SailorService;
import com.inlaco.crewmgrservice.utils.JsonMergePatchUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ContractServiceImpl implements ContractService {

  private final ContractRepository contractRepository;
  private final ContractVersionRepository contractVersionRepository;
  private final SailorService sailorService;
  private final JsonMergePatchUtils jsonMergePatch;
  private final CustomContractRepository customContractRepository;
  private final RentalRequestService rentalRequestService;
  private final CustomLaborContractRepository customLaborContractRepository;
  private final CandidateService candidateService;
  private final UploadFactory uploadFactory;

  @Override
  public AbstractContract getContractById(String id) {
    return contractRepository
        .findById(id)
        .orElseThrow(() -> new ResourceNotFoundException(AbstractContract.class, "id", id));
  }

  private ShortContract toShortContract(AbstractContract contract) {
    return ShortContract.builder()
        .id(contract.getId())
        .title(contract.getTitle())
        .type(contract.getType())
        .freezedAt(contract.getFreezeDate())
        .activationAt(contract.getActivationDate())
        .expiredAt(contract.getExpiredDate())
        .signed(contract.isSigned())
        .build();
  }

  @Override
  public Contract getLaborContractByEmployeeId(String employeeId) {
    return customLaborContractRepository.getLaborContractByEmployeeId(employeeId);
  }

  @Override
  public Contract saveContract(AbstractContract contract) {
    return contractRepository.save(contract);
  }

  @Override
  public ContractVersion saveVersion(ContractVersion contract) {
    String currentId = contract.getId();
    contract.setId(null);
    if (contract.isFirstVersion()) {
      String id = new ObjectId().toHexString();
      contract.setId(id);
      contract.setFirstVersionId(id);
    }

    var savedContract = contractVersionRepository.save(contract);
    if (savedContract.hasPrevVersion()) {
      var prevVersion =
          contractVersionRepository
              .findById(savedContract.getPrevVersionId())
              .orElseThrow(
                  () ->
                      new ResourceNotFoundException(
                          ContractVersion.class, "id", savedContract.getPrevVersionId()));

      prevVersion.setNextVersionId(savedContract.getId());
      prevVersion.setNextVersion(savedContract.getVersion());
      contractVersionRepository.save(prevVersion);
    }
    contract.setId(currentId);
    return savedContract;
  }

  @Override
  @Transactional
  public Contract createLaborContract(
      String candidateProfileId, LaborContract contract, String contractFileAssetId, User creator) {
    if (customLaborContractRepository.existsByCandidateProfileId(candidateProfileId)) {
      throw new ResourceAlreadyInUseException(
          LaborContract.class, "candidateProfileId", candidateProfileId);
    }

    CandidateProfile candidateProfile =
        candidateService.getCandidateProfileById(candidateProfileId);

    ObjectId accountId = candidateProfile.getAccountId();

    contract.setCandidateProfileId(new ObjectId(candidateProfileId));
    contract.setEmployeeId(accountId);

    var newContract = contractRepository.save(contract);

    log.info("Created labor contract for sailor with account id: {}", accountId);

    return newContract;
  }

  @Override
  @Transactional
  public Contract updateContract(String id, JsonNode patch, boolean newVersion) {
    AbstractContract contract = getContractById(id);
    if (contract.isFreezed()) {
      if (!newVersion) {
        throw new FreezeContractUpdateException(
            "Contract is freezed, please create a new contract or add sub terms");
      } else {
        var oldVersion = saveVersion(contract);

        AbstractContract newContract = jsonMergePatch.apply(contract, patch);

        if (oldVersion.isFirstVersion()) {
          newContract.setFirstVersionId(oldVersion.getId());
        }
        newContract.setPrevVersion(oldVersion.getVersion());
        newContract.setPrevVersionId(oldVersion.getId());
        newContract.setVersion(contract.getVersion() + 1);

        return contractRepository.save(newContract);
      }
    }
    return contractRepository.save(jsonMergePatch.apply(contract, patch));
  }

  @Override
  public ContractVersion getContractVersionById(String id) {
    return contractVersionRepository
        .findById(id)
        .orElseThrow(() -> new ResourceNotFoundException(ContractVersion.class, "id", id));
  }

  @Override
  public Page<? extends Contract> getAllContracts(
      ContractFilterable filterable, Pageable pageable) {
    return customContractRepository
        .findAllContracts(filterable, pageable)
        .map(this::toShortContract);
  }

  @Override
  @Transactional
  public Contract createSupplyContract(
      String requestId,
      SupplyContract contract,
      String contractFileAssetId,
      String shipImageAssetId,
      User creator) {
    var request = rentalRequestService.getRequestById(requestId);

    contract.setRentalRequestId(new ObjectId(request.getId()));
    contract.setContractFile(
        uploadFactory.metadata(UploadStrategy.CONTRACT_FILE, contractFileAssetId));
    contract
        .getShipInfo()
        .setImage(uploadFactory.metadata(UploadStrategy.SHIP_IMAGE, shipImageAssetId));

    var newContract = contractRepository.save(contract);

    request.setContractId(new ObjectId(newContract.getId()));
    request.setStatus(RentalRequestStatus.SIGNING);

    rentalRequestService.saveRequest(request);
    log.debug("Created supply contract for crew rental request with id: {}", requestId);

    return newContract;
  }

  @Override
  @Transactional
  public Contract activeContract(String contractId, User activer) {
    var contract = getContractById(contractId);
    contract.sign(new ObjectId(activer.getId()));
    log.debug("Actived contract with id: {}", contractId);
    return contractRepository.save(contract);
  }
}

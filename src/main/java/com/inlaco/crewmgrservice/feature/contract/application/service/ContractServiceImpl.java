package com.inlaco.crewmgrservice.feature.contract.application.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.inlaco.crewmgrservice.application.exception.ResourceAlreadyInUseException;
import com.inlaco.crewmgrservice.application.exception.ResourceNotFoundException;
import com.inlaco.crewmgrservice.feature.contract.application.port.in.ContractUseCase;
import com.inlaco.crewmgrservice.feature.contract.domain.exception.FreezeContractUpdateException;
import com.inlaco.crewmgrservice.feature.contract.domain.model.AbstractContract;
import com.inlaco.crewmgrservice.feature.contract.domain.model.Contract;
import com.inlaco.crewmgrservice.feature.contract.domain.model.ContractVersion;
import com.inlaco.crewmgrservice.feature.contract.domain.model.LaborContract;
import com.inlaco.crewmgrservice.feature.contract.domain.model.SupplyContract;
import com.inlaco.crewmgrservice.feature.contract.presentation.dto.ContractFilterable;
import com.inlaco.crewmgrservice.feature.contract.presentation.dto.ShortContract;
import com.inlaco.crewmgrservice.feature.contract.repository.ContractRepository;
import com.inlaco.crewmgrservice.feature.contract.repository.ContractVersionRepository;
import com.inlaco.crewmgrservice.feature.contract.repository.CustomContractRepository;
import com.inlaco.crewmgrservice.feature.contract.repository.CustomLaborContractRepository;
import com.inlaco.crewmgrservice.feature.crewrental.application.port.out.CrewRentalRequestRepository;
import com.inlaco.crewmgrservice.feature.crewrental.domain.enums.CrewRentalRequestStatus;
import com.inlaco.crewmgrservice.feature.crewrental.domain.model.CrewRentalRequest;
import com.inlaco.crewmgrservice.feature.recruitment.application.port.in.RecruitmentQueryUseCase;
import com.inlaco.crewmgrservice.feature.recruitment.domain.model.JobApplication;
import com.inlaco.crewmgrservice.feature.upload.application.enums.UploadStrategy;
import com.inlaco.crewmgrservice.feature.upload.application.port.in.UploadFactory;
import com.inlaco.crewmgrservice.feature.user.domain.model.User;
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
public class ContractServiceImpl implements ContractUseCase {

  private final ContractRepository contractRepository;
  private final ContractVersionRepository contractVersionRepository;
  private final JsonMergePatchUtils jsonMergePatch;
  private final CustomContractRepository customContractRepository;
  private final CrewRentalRequestRepository crewRentalRequestRepository;
  private final CustomLaborContractRepository customLaborContractRepository;
  private final RecruitmentQueryUseCase recruitmentQueryUseCase;
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

    JobApplication jobApplication =
        recruitmentQueryUseCase.getApplicationDetail(candidateProfileId);

    String accountId = jobApplication.getAccountId();

    contract.setCandidateProfileId(new ObjectId(candidateProfileId));
    contract.setEmployeeId(new ObjectId(accountId));

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
    var request =
        crewRentalRequestRepository
            .findById(requestId)
            .orElseThrow(
                () -> new ResourceNotFoundException(CrewRentalRequest.class, "id", requestId));

    contract.setRentalRequestId(new ObjectId(request.getId()));
    contract.setContractFile(
        uploadFactory.metadata(UploadStrategy.CONTRACT_FILE, contractFileAssetId));
    contract
        .getShipInfo()
        .setImage(uploadFactory.metadata(UploadStrategy.SHIP_IMAGE, shipImageAssetId));

    var newContract = contractRepository.save(contract);

    request.setContractId(newContract.getId());
    request.setStatus(CrewRentalRequestStatus.SIGNING);

    crewRentalRequestRepository.save(request);
    log.debug("Created supply contract for crew rental request with id: {}", requestId);

    return newContract;
  }

  @Override
  @Transactional
  public Contract signContract(String contractId, User activer) {
    var contract = getContractById(contractId);
    contract.sign(new ObjectId(activer.getId()));
    log.debug("Actived contract with id: {}", contractId);
    return contractRepository.save(contract);
  }
}

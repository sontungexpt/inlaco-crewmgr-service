package com.inlaco.crewmgrservice.feature.contract.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.inlaco.crewmgrservice.exceptions.ResourceNotFoundException;
import com.inlaco.crewmgrservice.feature.contract.dto.ContractFilterable;
import com.inlaco.crewmgrservice.feature.contract.dto.ShortContract;
import com.inlaco.crewmgrservice.feature.contract.event.ContractActivedEvent;
import com.inlaco.crewmgrservice.feature.contract.exception.FreezeContractUpdateException;
import com.inlaco.crewmgrservice.feature.contract.model.AbstractContract;
import com.inlaco.crewmgrservice.feature.contract.model.Contract;
import com.inlaco.crewmgrservice.feature.contract.model.ContractVersion;
import com.inlaco.crewmgrservice.feature.contract.model.LaborContract;
import com.inlaco.crewmgrservice.feature.contract.model.SupplyContract;
import com.inlaco.crewmgrservice.feature.contract.repository.ContractRepository;
import com.inlaco.crewmgrservice.feature.contract.repository.ContractVersionRepository;
import com.inlaco.crewmgrservice.feature.contract.repository.CustomContractRepository;
import com.inlaco.crewmgrservice.feature.contract.service.ContractService;
import com.inlaco.crewmgrservice.feature.crewhiring.enums.CrewRentalRequestStatus;
import com.inlaco.crewmgrservice.feature.crewhiring.service.CrewRentalRequestService;
import com.inlaco.crewmgrservice.feature.user.model.User;
import com.inlaco.crewmgrservice.feature.user.service.SailorService;
import com.inlaco.crewmgrservice.utils.JsonMergePatchUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.context.ApplicationEventPublisher;
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
  private final CrewRentalRequestService crewRentalRequestService;
  private final ApplicationEventPublisher eventPublisher;

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
        .createdAt(contract.getCreatedAt())
        .updatedAt(contract.getUpdatedAt())
        .signed(contract.isSigned())
        .build();
  }

  @Override
  public Page<? extends Contract> getSailorContracts(String sailorId, Pageable pageable) {
    var sailor = sailorService.findSailorProfileById(sailorId);
    if (sailor.getContractIds().isEmpty()) {
      throw new ResourceNotFoundException(AbstractContract.class, "sailorId", sailorId);
    }
    return contractRepository
        .findByPartyAccountIdsContains(sailor.getAccountId(), pageable)
        .map(this::toShortContract);
  }

  @Override
  public Contract saveContract(AbstractContract contract) {
    return contractRepository.save(contract);
  }

  @Override
  public ContractVersion saveVersion(ContractVersion contract) {
    return contractVersionRepository.save(contract);
  }

  @Override
  @Transactional
  public Contract createLaborContract(String sailorId, LaborContract contract, User creator) {
    var sailorProfile = sailorService.findSailorProfileById(sailorId);

    contract.getPartyAccountIds().add(sailorProfile.getAccountId());
    contract.getPartyAccountIds().add(new ObjectId(creator.getId()));
    contract.setEmployeeId(sailorProfile.getAccountId());

    var newContract = contractRepository.save(contract);

    sailorProfile.getContractIds().add(new ObjectId(newContract.getId()));

    sailorService.saveSailorProfile(sailorProfile);

    log.info("Created labor contract for sailor with id: {}", sailorId);

    return newContract;
  }

  @Override
  @Transactional
  public Contract updateContract(String id, JsonNode patch) {
    AbstractContract contract = getContractById(id);
    if (contract.isFreezed()) {
      throw new FreezeContractUpdateException(
          "Contract is freezed, please create a new contract or add sub terms");
    }
    return contractRepository.save((AbstractContract) jsonMergePatch.apply(contract, patch));
  }

  @Override
  public ContractVersion getContractVersionById(String id) {
    return contractVersionRepository
        .findById(id)
        .orElseThrow(() -> new ResourceNotFoundException(ContractVersion.class, "id", id));
  }

  @Override
  public Page<? extends Contract> getAllContracts(
      ContractFilterable filterRequest, Pageable pageable) {
    return customContractRepository
        .findAllContracts(filterRequest, pageable)
        .map(this::toShortContract);
  }

  @Override
  @Transactional
  public Contract createSupplyContract(
      String crewRentalRequestId, SupplyContract contract, User creator) {
    var crewRentalRequest = crewRentalRequestService.getRequestById(crewRentalRequestId);

    contract.getPartyAccountIds().add(crewRentalRequest.getCreatedBy());
    contract.getPartyAccountIds().add(new ObjectId(creator.getId()));
    contract.setRentalRequestId(new ObjectId(crewRentalRequest.getId()));

    var newContract = contractRepository.save(contract);

    crewRentalRequest.setContractId(new ObjectId(newContract.getId()));
    crewRentalRequest.setStatus(CrewRentalRequestStatus.SIGNING);

    crewRentalRequestService.saveRequest(crewRentalRequest);
    log.info("Created supply contract for crew rental request with id: {}", crewRentalRequestId);

    return newContract;
  }

  @Override
  @Transactional
  public Contract activeContract(String contractId, User activer) {
    var contract = getContractById(contractId);
    contract.sign(new ObjectId(activer.getId()));
    eventPublisher.publishEvent(new ContractActivedEvent(this, contract));
    log.info("Actived contract with id: {}", contractId);
    return contractRepository.save(contract);
  }
}

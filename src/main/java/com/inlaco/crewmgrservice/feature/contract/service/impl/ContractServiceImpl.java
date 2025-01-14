package com.inlaco.crewmgrservice.feature.contract.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.inlaco.crewmgrservice.exceptions.ResourceNotFoundException;
import com.inlaco.crewmgrservice.feature.contract.dto.ContractFilterable;
import com.inlaco.crewmgrservice.feature.contract.dto.ShortContract;
import com.inlaco.crewmgrservice.feature.contract.exception.FreezeContractUpdateException;
import com.inlaco.crewmgrservice.feature.contract.model.AbstractContract;
import com.inlaco.crewmgrservice.feature.contract.model.Contract;
import com.inlaco.crewmgrservice.feature.contract.model.ContractVersion;
import com.inlaco.crewmgrservice.feature.contract.repository.ContractRepository;
import com.inlaco.crewmgrservice.feature.contract.repository.ContractVersionRepository;
import com.inlaco.crewmgrservice.feature.contract.repository.CustomContractRepository;
import com.inlaco.crewmgrservice.feature.contract.service.ContractService;
import com.inlaco.crewmgrservice.feature.user.model.User;
import com.inlaco.crewmgrservice.feature.user.service.SailorService;
import com.inlaco.crewmgrservice.utils.JsonMergePatchUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ContractServiceImpl implements ContractService {

  private final ContractRepository contractRepository;
  private final ContractVersionRepository contractVersionRepository;
  private final SailorService sailorService;
  private final JsonMergePatchUtils jsonMergePatch;
  private final CustomContractRepository customContractRepository;

  @Override
  public AbstractContract getContractById(String id) {
    return contractRepository
        .findById(id)
        .orElseThrow(() -> new ResourceNotFoundException(AbstractContract.class, "id", id));
  }

  @Override
  public Page<? extends Contract> getSailorContracts(String sailorId, Pageable pageable) {
    var sailor = sailorService.findSailorProfileById(sailorId);
    if (sailor.getContractIds().isEmpty()) {
      throw new ResourceNotFoundException(AbstractContract.class, "sailorId", sailorId);
    }
    return contractRepository
        .findByPartyAccountIdsContains(sailor.getAccountId(), pageable)
        .map(
            it ->
                ShortContract.builder()
                    .id(it.getId())
                    .title(it.getTitle())
                    .type(it.getType())
                    .freezedAt(it.getFreezeDate())
                    .createdAt(it.getCreatedAt())
                    .updatedAt(it.getUpdatedAt())
                    .signed(it.isSigned())
                    .build());
  }

  @Override
  public Contract addContract(AbstractContract contract) {
    throw new UnsupportedOperationException("Unimplemented method 'addContract'");
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
  public Contract createSailorLaborContract(
      String sailorId, AbstractContract contract, User creator) {
    var sailorProfile = sailorService.findSailorProfileById(sailorId);

    contract.getPartyAccountIds().add(sailorProfile.getAccountId());
    contract.getPartyAccountIds().add(new ObjectId(creator.getId()));

    var newContract = contractRepository.save(contract);

    sailorProfile.getContractIds().add(new ObjectId(newContract.getId()));

    sailorService.saveSailorProfile(sailorProfile);

    return newContract;
  }

  @Override
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
        .map(
            it ->
                ShortContract.builder()
                    .id(it.getId())
                    .title(it.getTitle())
                    .type(it.getType())
                    .freezedAt(it.getFreezeDate())
                    .createdAt(it.getCreatedAt())
                    .updatedAt(it.getUpdatedAt())
                    .signed(it.isSigned())
                    .build());
  }

  @Override
  public Contract createSupplierContract(AbstractContract contract, User creator) {
    throw new UnsupportedOperationException("Unimplemented method 'createSupplierContract'");
  }
}

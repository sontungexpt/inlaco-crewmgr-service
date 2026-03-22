package com.inlaco.crewmgrservice.feature.contract.application.service;

import com.inlaco.crewmgrservice.feature.contract.application.model.ContractSearchCriteria;
import com.inlaco.crewmgrservice.feature.contract.application.port.in.ContractQueryUseCase;
import com.inlaco.crewmgrservice.feature.contract.application.port.out.ContractRepository;
import com.inlaco.crewmgrservice.feature.contract.application.port.out.ContractSnapshotRepository;
import com.inlaco.crewmgrservice.feature.contract.domain.model.Contract;
import com.inlaco.crewmgrservice.shared.kernel.exception.ResourceNotFoundException;
import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ContractQueryService implements ContractQueryUseCase {

  private final ContractSnapshotRepository contractSnapshotRepository;
  private final ContractRepository contractRepository;

  @Override
  public Contract getContract(String id) {
    return contractRepository
        .findById(id)
        .orElseThrow(() -> new ResourceNotFoundException(Contract.class, "id", id));
  }

  @Override
  public Page<? extends Contract> getContracts(ContractSearchCriteria criteria, Pageable pageable) {
    return contractRepository.findAll(criteria, pageable);
  }

  @Override
  public List<Contract> getOldContractVersions(String contractId) {
    List<Contract> snapshots = contractSnapshotRepository.findByContractId(contractId);

    if (snapshots.isEmpty()) {
      throw new ResourceNotFoundException(Contract.class, "contractId", contractId);
    }

    return snapshots.stream().sorted(Comparator.comparing(Contract::getVersion)).toList();
  }
}

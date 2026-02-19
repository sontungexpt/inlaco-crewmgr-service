package com.inlaco.crewmgrservice.feature.contract.application.service;

import com.inlaco.crewmgrservice.feature.contract.application.port.in.UpdateContractUseCase;
import com.inlaco.crewmgrservice.feature.contract.application.port.out.ContractRepository;
import com.inlaco.crewmgrservice.feature.contract.application.port.out.ContractSnapshotRepository;
import com.inlaco.crewmgrservice.feature.contract.domain.model.Contract;
import com.inlaco.crewmgrservice.feature.contract.domain.model.UpdateContractCommand;
import com.inlaco.crewmgrservice.shared.kernel.exception.ResourceNotFoundException;
import java.time.Instant;
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

  @Override
  @Transactional
  public Contract update(String id, UpdateContractCommand patch) {
    Contract current =
        contractRepository
            .findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(Contract.class, "id", id));

    Instant now = Instant.now();
    if (current.isFreezed(now)) {
      contractSnapshotRepository.save(current);
    }

    current.amend(patch, now);
    return contractRepository.save(current);
  }
}

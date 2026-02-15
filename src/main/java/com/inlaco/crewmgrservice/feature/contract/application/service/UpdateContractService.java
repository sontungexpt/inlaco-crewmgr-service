package com.inlaco.crewmgrservice.feature.contract.application.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.inlaco.crewmgrservice.feature.contract.application.port.in.UpdateContractUseCase;
import com.inlaco.crewmgrservice.feature.contract.application.port.out.ContractRepository;
import com.inlaco.crewmgrservice.feature.contract.application.port.out.ContractSnapshotRepository;
import com.inlaco.crewmgrservice.feature.contract.domain.exception.FreezeContractUpdateException;
import com.inlaco.crewmgrservice.feature.contract.domain.model.AbstractContract;
import com.inlaco.crewmgrservice.shared.kernel.exception.ResourceNotFoundException;
import com.inlaco.crewmgrservice.shared.support.JsonMergePatchUtils;
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
  private final JsonMergePatchUtils jsonMergePatchUtils;

  @Override
  @Transactional
  public AbstractContract update(String id, JsonNode patch) {
    AbstractContract current =
        contractRepository
            .findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(AbstractContract.class, "id", id));

    if (current.isFreezed(Instant.now())) {
      throw new FreezeContractUpdateException(
          "Contract is freezed, please create a new contract or add sub terms");
    }

    var snapshot = contractSnapshotRepository.save(current);

    // jsonMergePatchUtils.apply(current, patch);

    current.incrementVersion();

    return contractRepository.save(current);
  }
}

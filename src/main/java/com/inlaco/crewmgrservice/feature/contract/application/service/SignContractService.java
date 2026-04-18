package com.inlaco.crewmgrservice.feature.contract.application.service;

import com.inlaco.crewmgrservice.feature.contract.application.port.in.SignContractUseCase;
import com.inlaco.crewmgrservice.feature.contract.application.port.out.ContractRepository;
import com.inlaco.crewmgrservice.feature.contract.domain.event.ContractSignedEvent;
import com.inlaco.crewmgrservice.feature.contract.domain.model.Contract;
import com.inlaco.crewmgrservice.feature.user.domain.model.User;
import com.inlaco.crewmgrservice.shared.kernel.exception.ResourceNotFoundException;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SignContractService implements SignContractUseCase {

  private final ContractRepository contractRepository;
  private final ApplicationEventPublisher eventPublisher;

  @Override
  @Transactional(rollbackFor = Exception.class)
  public Contract sign(String contractId, User signer) {
    var contract =
        contractRepository
            .findById(contractId)
            .orElseThrow(() -> new ResourceNotFoundException(Contract.class, "id", contractId));

    contract.validateForSigning();
    contract.sign(signer.getId(), Instant.now());
    log.debug("Actived contract with id: {}", contractId);
    var signedContract = contractRepository.save(contract);
    eventPublisher.publishEvent(new ContractSignedEvent(signedContract));
    return signedContract;
  }
}

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
  @Transactional
  public Contract sign(String contractId, User signer) {
    var contract =
        contractRepository
            .findById(contractId)
            .orElseThrow(() -> new ResourceNotFoundException(Contract.class, "id", contractId));

    validateContract(contract);

    contract.sign(signer.getId(), Instant.now());
    log.debug("Actived contract with id: {}", contractId);
    var signedContract = contractRepository.save(contract);
    eventPublisher.publishEvent(new ContractSignedEvent(signedContract));
    return signedContract;
  }

  private void validateContract(Contract contract) {

    // 1️⃣ Status phải là DRAFT
    if (!contract.isDraft()) {
      throw new IllegalStateException("Only draft contracts can be signed");
    }

    // 2️⃣ Title
    if (contract.getTitle() == null || contract.getTitle().isBlank()) {
      throw new IllegalArgumentException("Contract title is required");
    }

    // 3️⃣ Initiator
    if (contract.getInitiator() == null) {
      throw new IllegalArgumentException("Initiator is required");
    }

    // 4️⃣ Partners
    if (contract.getPartners() == null || contract.getPartners().isEmpty()) {
      throw new IllegalArgumentException("At least one partner is required");
    }

    // 5️⃣ Contract file
    if (contract.getContractFile() == null) {
      throw new IllegalArgumentException("Contract file is required");
    }

    // 6️⃣ Activation date
    if (contract.getActivationDate() == null) {
      throw new IllegalArgumentException("Activation date is required");
    }

    // 7️⃣ Expired date
    if (contract.getExpiredDate() != null
        && contract.getActivationDate() != null
        && contract.getExpiredDate().isBefore(contract.getActivationDate())) {
      throw new IllegalArgumentException("Expired date must be after activation date");
    }
  }
}

package com.inlaco.crewmgrservice.feature.crew.infrastructure.listener;

import com.inlaco.crewmgrservice.feature.contract.domain.event.ContractSignedEvent;
import com.inlaco.crewmgrservice.feature.contract.domain.model.LaborContract;
import com.inlaco.crewmgrservice.feature.contract.domain.model.party.LaborParty;
import com.inlaco.crewmgrservice.feature.crew.application.port.in.CrewUseCase;
import com.inlaco.crewmgrservice.feature.crew.domain.model.ApplyLaborContractCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
@Slf4j
public class CrewContractSignedEventListener {

  private final CrewUseCase crewUseCase;

  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void handle(ContractSignedEvent event) {
    if (!(event.contract() instanceof LaborContract contract)) {
      return;
    }

    // Reduced verbosity for high-frequency events: keep receipt at DEBUG level.
    log.debug("Received ContractSignedEvent for LaborContract with ID: {}", contract.getId());

    LaborParty party = (LaborParty) contract.getPartners().get(0);

    // Applying the contract is an important state change — keep it at INFO.
    log.info("Applying labor contract for account ID: {}", contract.getAccountId());
    crewUseCase.applyLaborContract(
        new ApplyLaborContractCommand(
            contract.getAccountId(),
            party.getRepresenter(),
            party.getAddress(),
            party.getPhone(),
            party.getEmail(),
            party.getBirthDate(),
            contract.getPosition(),
            party.getIdentificationCardId()));
  }
}

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

  @TransactionalEventListener(
      value = ContractSignedEvent.class,
      phase = TransactionPhase.AFTER_COMMIT)
  public void handle(ContractSignedEvent event) {
    if (!(event.contract() instanceof LaborContract contract)) {
      return;
    }

    LaborParty party = (LaborParty) contract.getPartners().get(0);

    crewUseCase.applyLaborContract(
        new ApplyLaborContractCommand(
            contract.getEmployeeId(),
            party.getRepresenter(),
            party.getAddress(),
            party.getPhone(),
            party.getEmail(),
            party.getBirthDate(),
            contract.getPosition()));
  }
}

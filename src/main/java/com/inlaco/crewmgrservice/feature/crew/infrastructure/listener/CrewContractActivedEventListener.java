package com.inlaco.crewmgrservice.feature.crew.infrastructure.listener;

import com.inlaco.crewmgrservice.feature.contract.domain.event.ContractActivedEvent;
import com.inlaco.crewmgrservice.feature.contract.domain.model.LaborContract;
import com.inlaco.crewmgrservice.feature.crew.application.port.out.CrewProfileRepository;
import com.inlaco.crewmgrservice.feature.crew.domain.enums.CrewStatus;
import com.inlaco.crewmgrservice.feature.crew.domain.model.CrewProfile;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
@Slf4j
public class CrewContractActivedEventListener {

  private final CrewProfileRepository repository;

  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void handle(ContractActivedEvent event) {
    log.info("Received ContractActivedEvent with {} contracts", event.contracts().size());

    List<String> accountIds =
        event.contracts().stream()
            .filter(c -> c instanceof LaborContract)
            .map(c -> ((LaborContract) c).getAccountId())
            .toList();

    log.info("Extracted {} labor contract accountIds", accountIds.size());

    if (accountIds.isEmpty()) {
      log.info("No labor contracts found. Skipping crew status update.");
      return;
    }

    List<CrewProfile> profiles = repository.findAllByAccountId(accountIds);

    log.info("Found {} crew profiles to update", profiles.size());

    for (CrewProfile profile : profiles) {
      profile.changeStatus(CrewStatus.READY_FOR_ASSIGNMENT);
    }

    repository.saveAll(profiles);

    log.info(
        "Updated {} crew profiles to status {}", profiles.size(), CrewStatus.READY_FOR_ASSIGNMENT);
  }
}

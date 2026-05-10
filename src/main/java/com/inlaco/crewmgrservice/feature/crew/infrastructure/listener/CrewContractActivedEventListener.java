package com.inlaco.crewmgrservice.feature.crew.infrastructure.listener;

import com.inlaco.crewmgrservice.feature.contract.domain.event.ContractActivedEvent;
import com.inlaco.crewmgrservice.feature.contract.domain.model.LaborContract;
import com.inlaco.crewmgrservice.feature.crew.application.port.out.CrewProfileRepository;
import com.inlaco.crewmgrservice.feature.crew.domain.enums.CrewOperationalStatus;
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
    log.debug("Handling ContractActivedEvent with {} contract(s)", event.contracts().size());

    List<String> accountIds =
        event.contracts().stream()
            .filter(
                c -> {
                  if (!(c instanceof LaborContract)) {
                    // Not an error condition — just not relevant for crew activation.
                    log.debug("Skipping non-labor contract with id={}", c.getId());
                    return false;
                  }
                  return true;
                })
            .map(
                c -> {
                  String accountId = ((LaborContract) c).getAccountId();
                  // Detailed extraction is trace-level since it can be noisy in production.
                  log.trace("Extracted accountId={} from contractId={}", accountId, c.getId());
                  return accountId;
                })
            .toList();

    log.debug("Extracted {} labor contract accountId(s)", accountIds.size());

    if (accountIds.isEmpty()) {
      // Informational: normal case when no labor contracts present.
      log.info("No labor contracts found. Skipping crew status update.");
      return;
    }

    List<CrewProfile> profiles = repository.findAllByAccountId(accountIds);

    log.info("Found {} crew profile(s) to update", profiles.size());

    var newStatus = CrewOperationalStatus.AVAILABLE;
    for (CrewProfile profile : profiles) {
      try {
        profile.changeStatus(newStatus);
        // Success for each profile can be verbose; keep it trace-level.
        log.trace("Changed status for crew profile id={} to {}", profile.getId(), newStatus);
      } catch (IllegalStateException e) {
        // This is a recoverable domain issue for a single profile; warn and continue.
        log.warn(
            "Failed to change status for crew profile id={} to {}. Reason: {}",
            profile.getId(),
            newStatus,
            e.getMessage(),
            e);
      }
    }

    // Persist updates
    repository.saveAll(profiles);
    log.info("Updated {} crew profile(s) to status {}", profiles.size(), newStatus);
  }
}

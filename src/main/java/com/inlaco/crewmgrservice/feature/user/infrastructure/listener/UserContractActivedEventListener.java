package com.inlaco.crewmgrservice.feature.user.infrastructure.listener;

import com.inlaco.crewmgrservice.feature.contract.domain.event.ContractActivedEvent;
import com.inlaco.crewmgrservice.feature.contract.domain.model.LaborContract;
import com.inlaco.crewmgrservice.feature.user.application.port.in.UserUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserContractActivedEventListener {

  private final UserUseCase userService;

  @TransactionalEventListener
  public void handle(ContractActivedEvent event) {
    log.info("Received ContractActivedEvent with {} contracts", event.contracts().size());

    for (var c : event.contracts()) {
      if (!(c instanceof LaborContract contract)) {
        // Non-labor contracts are ignored for user role assignment; debug log for traceability
        try {
          log.debug("Skipping non-labor contract with id={}", c.getId());
        } catch (Exception e) {
          // Defensive: if contract id access fails for some reason, still continue
          log.debug("Skipping non-labor contract (failed to read id): {}", e.getMessage());
        }
        continue;
      }

      log.debug("Assigning role 'SAILOR' to accountId={}", contract.getAccountId());
      userService.assignRole(contract.getAccountId(), "SAILOR");
    }
  }
}

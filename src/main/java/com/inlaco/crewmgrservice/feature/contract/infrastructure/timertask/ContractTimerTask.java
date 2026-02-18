package com.inlaco.crewmgrservice.feature.contract.infrastructure.timertask;

import com.inlaco.crewmgrservice.feature.contract.application.port.in.ContractLifecycleUseCase;
import com.inlaco.crewmgrservice.feature.contract.application.port.out.ContractRepository;
import com.inlaco.crewmgrservice.feature.contract.domain.model.Contract;
import java.time.Instant;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ContractTimerTask {

  private final ContractRepository contractRepository;
  private final ContractLifecycleUseCase contractActivationUseCase;

  /** Runs every minute to activate contracts whose activationDate has passed. */
  @Scheduled(cron = "0 0/1 * * * ?")
  public void activateSignedContracts() {
    Instant now = Instant.now();
    List<Contract> contracts = contractRepository.findDueForActivation(now);
    if (contracts.isEmpty()) {
      log.debug("No contracts due for activation at {}", now);
      return;
    }
    log.info("Found {} contract(s) to activate", contracts.size());
    contractActivationUseCase.activateDueContracts(contracts, now);
  }

  @Scheduled(cron = "0 0/1 * * * ?")
  public void expireContracts() {
    Instant now = Instant.now();
    List<Contract> contracts = contractRepository.findDueForExpiration(now);
    if (contracts.isEmpty()) {
      log.debug("No contracts due for expiration at {}", now);
      return;
    }
    log.info("Found {} contract(s) to expire", contracts.size());
    contractActivationUseCase.expireContracts(contracts, now);
  }
}

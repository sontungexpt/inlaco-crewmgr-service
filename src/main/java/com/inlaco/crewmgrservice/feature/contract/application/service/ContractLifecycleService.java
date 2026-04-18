package com.inlaco.crewmgrservice.feature.contract.application.service;

import com.inlaco.crewmgrservice.feature.contract.application.port.in.ContractLifecycleUseCase;
import com.inlaco.crewmgrservice.feature.contract.application.port.out.ContractRepository;
import com.inlaco.crewmgrservice.feature.contract.domain.event.ContractActivedEvent;
import com.inlaco.crewmgrservice.feature.contract.domain.event.ContractExpiredEvent;
import com.inlaco.crewmgrservice.feature.contract.domain.model.Contract;
import java.time.Instant;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ContractLifecycleService implements ContractLifecycleUseCase {

  private final ContractRepository contractRepository;
  private final ApplicationEventPublisher eventPublisher;

  @Override
  @Transactional
  public void activateDueContracts(List<Contract> contracts, Instant now) {
    log.info("Activating due contracts at {}", now);
    for (var contract : contracts) {
      log.debug("Activating contract with ID: {}", contract.getId());
      contract.activate(now);
    }
    eventPublisher.publishEvent(
        new ContractActivedEvent(contractRepository.saveAll(contracts), now));
    log.info("Successfully activated {} contracts", contracts.size());
  }

  @Override
  @Transactional
  public void expireContracts(List<Contract> contracts, Instant now) {
    log.info("Expiring contracts at {}", now);
    for (var contract : contracts) {
      log.debug("Expiring contract with ID: {}", contract.getId());
      contract.expire(now);
    }
    eventPublisher.publishEvent(
        new ContractExpiredEvent(contractRepository.saveAll(contracts), now));
    log.info("Successfully expired {} contracts", contracts.size());
  }
}

package com.inlaco.crewmgrservice.feature.contract.application.service;

import com.inlaco.crewmgrservice.feature.contract.application.port.in.ContractLifecycleUseCase;
import com.inlaco.crewmgrservice.feature.contract.application.port.out.ContractRepository;
import com.inlaco.crewmgrservice.feature.contract.domain.event.ContractActivedEvent;
import com.inlaco.crewmgrservice.feature.contract.domain.event.ContractExpiredEvent;
import com.inlaco.crewmgrservice.feature.contract.domain.model.AbstractContract;
import java.time.Instant;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ContractLifecycleService implements ContractLifecycleUseCase {

  private final ContractRepository contractRepository;
  private final ApplicationEventPublisher eventPublisher;

  @Override
  @Transactional
  public void activateDueContracts(List<AbstractContract> contracts, Instant now) {
    for (var contract : contracts) {
      contract.activate(now);
    }
    eventPublisher.publishEvent(
        new ContractActivedEvent(contractRepository.saveAll(contracts), now));
  }

  @Override
  @Transactional
  public void expireContracts(List<AbstractContract> contracts, Instant now) {
    for (var contract : contracts) {
      contract.expire(now);
    }
    eventPublisher.publishEvent(
        new ContractExpiredEvent(contractRepository.saveAll(contracts), now));
  }
}

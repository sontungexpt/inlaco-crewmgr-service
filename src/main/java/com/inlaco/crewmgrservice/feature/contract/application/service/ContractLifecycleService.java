package com.inlaco.crewmgrservice.feature.contract.application.service;

import com.inlaco.crewmgrservice.feature.contract.application.port.in.ContractLifecycleUseCase;
import com.inlaco.crewmgrservice.feature.contract.application.port.out.ContractRepository;
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
      contract.broadcast(eventPublisher::publishEvent);
    }
    contractRepository.saveAll(contracts);
  }

  @Override
  @Transactional
  public void expireContracts(List<AbstractContract> contracts, Instant now) {
    for (var contract : contracts) {
      contract.expire(now);
      contract.broadcast(eventPublisher::publishEvent);
    }
    contractRepository.saveAll(contracts);
  }

  //   private void activateSupplyContract(SupplyContract contract) {
}

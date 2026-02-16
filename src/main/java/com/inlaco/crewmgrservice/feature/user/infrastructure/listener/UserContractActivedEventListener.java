package com.inlaco.crewmgrservice.feature.user.infrastructure.listener;

import com.inlaco.crewmgrservice.feature.contract.domain.event.ContractActivedEvent;
import com.inlaco.crewmgrservice.feature.contract.domain.model.LaborContract;
import com.inlaco.crewmgrservice.feature.user.application.port.in.UserUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class UserContractActivedEventListener {
  private final UserUseCase userService;

  @TransactionalEventListener
  public void handle(ContractActivedEvent event) {
    for (var c : event.contracts()) {
      if (!(c instanceof LaborContract contract)) continue;
      userService.assignRole(contract.getAccountId(), "SAILOR");
    }
  }
}

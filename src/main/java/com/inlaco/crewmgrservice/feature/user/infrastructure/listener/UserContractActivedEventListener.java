package com.inlaco.crewmgrservice.feature.user.infrastructure.listener;

import com.inlaco.crewmgrservice.feature.contract.domain.event.ContractActivedEvent;
import com.inlaco.crewmgrservice.feature.contract.domain.model.LaborContract;
import com.inlaco.crewmgrservice.feature.user.application.port.in.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserContractActivedEventListener {
  private final UserService userService;

  @EventListener
  public void handle(ContractActivedEvent event) {
    for (var c : event.contracts()) {
      if (!(c instanceof LaborContract contract)) continue;
      userService.updateToSailor(contract.getAccountId());
    }
  }
}

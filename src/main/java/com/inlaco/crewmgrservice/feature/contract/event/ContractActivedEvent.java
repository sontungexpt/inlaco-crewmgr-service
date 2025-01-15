package com.inlaco.crewmgrservice.feature.contract.event;

import com.inlaco.crewmgrservice.feature.contract.model.Contract;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class ContractActivedEvent extends ApplicationEvent {

  private Contract contract;

  public ContractActivedEvent(Object source, Contract contract) {
    super(source);
    this.contract = contract;
  }
}

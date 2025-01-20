package com.inlaco.crewmgrservice.feature.user.event;

import com.inlaco.crewmgrservice.feature.contract.model.LaborContract;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class SailorOfficalEvent extends ApplicationEvent {

  private final LaborContract laborContract;

  public SailorOfficalEvent(Object source, LaborContract laborContract) {
    super(source);
    this.laborContract = laborContract;
  }
}

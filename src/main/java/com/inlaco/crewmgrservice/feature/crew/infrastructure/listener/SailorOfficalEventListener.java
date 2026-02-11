package com.inlaco.crewmgrservice.feature.crew.infrastructure.listener;

import com.inlaco.crewmgrservice.feature.crew.application.event.SailorOfficalEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class SailorOfficalEventListener {

  @TransactionalEventListener(SailorOfficalEvent.class)
  public void handleSailorOfficalEvent(SailorOfficalEvent event) {}
}

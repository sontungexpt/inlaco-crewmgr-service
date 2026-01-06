package com.inlaco.crewmgrservice.feature.user.event.listener;

import com.inlaco.crewmgrservice.feature.user.event.SailorOfficalEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class SailorOfficalEventListener {

  @TransactionalEventListener(SailorOfficalEvent.class)
  public void handleSailorOfficalEvent(SailorOfficalEvent event) {}
}

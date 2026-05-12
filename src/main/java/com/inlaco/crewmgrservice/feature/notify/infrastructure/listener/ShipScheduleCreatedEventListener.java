package com.inlaco.crewmgrservice.feature.notify.infrastructure.listener;

import com.inlaco.crewmgrservice.feature.notify.application.port.in.ShipScheduleNotificationUseCase;
import com.inlaco.crewmgrservice.feature.shipschedule.domain.event.ShipScheduleCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@Slf4j
@RequiredArgsConstructor
public class ShipScheduleCreatedEventListener {

  private final ShipScheduleNotificationUseCase shipScheduleNotificationUseCase;

  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void handleShipScheduleCreatedEvent(ShipScheduleCreatedEvent event) {
    shipScheduleNotificationUseCase.notifyUsers(
        event.shipSchedule(), event.assignments(), event.crewProfiles());
  }
}

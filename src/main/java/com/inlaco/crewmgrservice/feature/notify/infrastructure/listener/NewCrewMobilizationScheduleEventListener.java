package com.inlaco.crewmgrservice.feature.notify.infrastructure.listener;

import com.inlaco.crewmgrservice.feature.crewmobilization.domain.event.NewCrewMobilizationEvent;
import com.inlaco.crewmgrservice.feature.notify.application.port.in.CrewMobilizationNotificationUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@Slf4j
@RequiredArgsConstructor
public class NewCrewMobilizationScheduleEventListener {

  private final CrewMobilizationNotificationUseCase mobilizationScheduleNotificationUseCase;

  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void handleNewCrewMobilizationScheduleEvent(NewCrewMobilizationEvent event) {
    mobilizationScheduleNotificationUseCase.notifyUsers(event.schedule());
  }
}

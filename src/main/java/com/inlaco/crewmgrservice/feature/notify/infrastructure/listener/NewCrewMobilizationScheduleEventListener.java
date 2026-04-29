package com.inlaco.crewmgrservice.feature.notify.infrastructure.listener;

import com.inlaco.crewmgrservice.feature.notify.application.port.in.MobilizationScheduleNotificationUseCase;
import com.inlaco.crewmgrservice.feature.schedule.domain.event.NewCrewMobilizationScheduleEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@Slf4j
@RequiredArgsConstructor
public class NewCrewMobilizationScheduleEventListener {

  private final MobilizationScheduleNotificationUseCase mobilizationScheduleNotificationUseCase;

  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void handleNewCrewMobilizationScheduleEvent(NewCrewMobilizationScheduleEvent event) {
    mobilizationScheduleNotificationUseCase.notifyUsers(event.schedule());
  }
}

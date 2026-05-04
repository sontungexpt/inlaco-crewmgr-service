package com.inlaco.crewmgrservice.feature.notify.application.port.in;

import com.inlaco.crewmgrservice.feature.crewmobilization.domain.model.CrewMobilizationSchedule;

public interface CrewMobilizationNotificationUseCase {

  void notifyUsers(CrewMobilizationSchedule schedule);
}

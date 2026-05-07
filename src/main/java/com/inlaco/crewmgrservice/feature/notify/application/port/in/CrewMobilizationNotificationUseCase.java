package com.inlaco.crewmgrservice.feature.notify.application.port.in;

import com.inlaco.crewmgrservice.feature.crewmobilization.domain.model.CrewMobilization;

public interface CrewMobilizationNotificationUseCase {

  void notifyUsers(CrewMobilization schedule);
}

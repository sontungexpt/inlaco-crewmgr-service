package com.inlaco.crewmgrservice.feature.notify.application.port.in;

import com.inlaco.crewmgrservice.feature.schedule.domain.model.CrewMobilizationSchedule;

public interface MobilizationScheduleNotificationUseCase {

  void notifyUsers(CrewMobilizationSchedule schedule);
}

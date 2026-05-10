package com.inlaco.crewmgrservice.feature.shipschedule.application.port.in;

import com.inlaco.crewmgrservice.feature.shipschedule.domain.model.ShipSchedule;

public interface ShipScheduleUseCase {
  ShipSchedule createSchedule(ShipSchedule schedule);
}

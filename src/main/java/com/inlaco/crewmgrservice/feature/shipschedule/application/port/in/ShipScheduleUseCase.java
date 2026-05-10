package com.inlaco.crewmgrservice.feature.shipschedule.application.port.in;

import com.inlaco.crewmgrservice.feature.shipschedule.domain.model.ShipSchedule;
import com.inlaco.crewmgrservice.feature.shipschedule.domain.model.ShipScheduleCrewAssignment;
import java.util.List;

public interface ShipScheduleUseCase {
  ShipSchedule createSchedule(ShipSchedule schedule, List<ShipScheduleCrewAssignment> assignments);
}

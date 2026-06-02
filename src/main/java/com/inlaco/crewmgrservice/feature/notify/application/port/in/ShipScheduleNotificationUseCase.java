package com.inlaco.crewmgrservice.feature.notify.application.port.in;

import com.inlaco.crewmgrservice.feature.crew.domain.model.CrewProfile;
import com.inlaco.crewmgrservice.feature.shipschedule.domain.model.ShipSchedule;
import com.inlaco.crewmgrservice.feature.shipschedule.domain.model.ShipScheduleCrewAssignment;
import java.util.List;

public interface ShipScheduleNotificationUseCase {

  void notifyUsers(ShipSchedule shipSchedule, List<ShipScheduleCrewAssignment> assignments, List<CrewProfile> crewProfiles);
}

package com.inlaco.crewmgrservice.feature.shipschedule.domain.event;

import com.inlaco.crewmgrservice.feature.crew.domain.model.CrewProfile;
import com.inlaco.crewmgrservice.feature.shipschedule.domain.model.ShipSchedule;
import com.inlaco.crewmgrservice.feature.shipschedule.domain.model.ShipScheduleCrewAssignment;
import java.util.List;

public record ShipScheduleCreatedEvent(
    ShipSchedule shipSchedule,
    List<ShipScheduleCrewAssignment> assignments,
    List<CrewProfile> crewProfiles) {}

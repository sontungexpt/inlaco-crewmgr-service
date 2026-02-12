package com.inlaco.crewmgrservice.feature.schedule.domain.event;

import com.inlaco.crewmgrservice.feature.schedule.domain.model.CrewMobilizationSchedule;

public record NewCrewMobilizationScheduleEvent(CrewMobilizationSchedule schedule) {}

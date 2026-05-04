package com.inlaco.crewmgrservice.feature.crewmobilization.domain.event;

import com.inlaco.crewmgrservice.feature.crewmobilization.domain.model.CrewMobilizationSchedule;

public record NewCrewMobilizationEvent(CrewMobilizationSchedule schedule) {}

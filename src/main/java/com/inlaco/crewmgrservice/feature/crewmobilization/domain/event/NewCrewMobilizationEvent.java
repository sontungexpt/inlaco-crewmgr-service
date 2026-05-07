package com.inlaco.crewmgrservice.feature.crewmobilization.domain.event;

import com.inlaco.crewmgrservice.feature.crewmobilization.domain.model.CrewMobilization;

public record NewCrewMobilizationEvent(CrewMobilization schedule) {}

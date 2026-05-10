package com.inlaco.crewmgrservice.feature.crewmobilization.domain.event;

import com.inlaco.crewmgrservice.feature.crew.domain.model.CrewProfile;
import com.inlaco.crewmgrservice.feature.crewmobilization.domain.model.CrewMobilization;
import java.util.List;

public record NewCrewMobilizationEvent(
    CrewMobilization mobilization, List<CrewProfile> crewProfiles) {}

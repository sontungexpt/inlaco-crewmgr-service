package com.inlaco.crewmgrservice.feature.crewmobilization.application.port.in;

import com.inlaco.crewmgrservice.feature.crewmobilization.domain.model.CrewMobilizationSchedule;

public interface CrewMobilizationCommandUseCase {

  CrewMobilizationSchedule createSchedule(CrewMobilizationSchedule schedule);

  // CrewMobilizationSchedule updateSchedule(String id, JsonNode patch);

}

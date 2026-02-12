package com.inlaco.crewmgrservice.feature.schedule.application.port.in;

import com.inlaco.crewmgrservice.feature.schedule.domain.model.CrewMobilizationSchedule;

public interface CrewMobilizationScheduleCommandUseCase {

  CrewMobilizationSchedule createSchedule(CrewMobilizationSchedule schedule);

  // CrewMobilizationSchedule updateSchedule(String id, JsonNode patch);

}

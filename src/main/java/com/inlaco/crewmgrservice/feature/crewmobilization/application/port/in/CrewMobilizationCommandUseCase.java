package com.inlaco.crewmgrservice.feature.crewmobilization.application.port.in;

import com.inlaco.crewmgrservice.feature.crewmobilization.domain.model.CrewMobilization;
import com.inlaco.crewmgrservice.feature.user.domain.model.User;

public interface CrewMobilizationCommandUseCase {

  CrewMobilization createMobilization(
      CrewMobilization schedule, String shipImageAssetId, User user);

  // CrewMobilizationSchedule updateSchedule(String id, JsonNode patch);

}

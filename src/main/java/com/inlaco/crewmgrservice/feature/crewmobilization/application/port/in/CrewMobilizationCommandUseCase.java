package com.inlaco.crewmgrservice.feature.crewmobilization.application.port.in;

import com.inlaco.crewmgrservice.feature.crewmobilization.domain.model.CrewMobilization;
import com.inlaco.crewmgrservice.feature.crewmobilization.domain.model.CrewMobilizationAssignment;
import com.inlaco.crewmgrservice.feature.user.domain.model.User;
import java.util.List;

public interface CrewMobilizationCommandUseCase {

  CrewMobilization createMobilization(
      CrewMobilization schedule,
      List<CrewMobilizationAssignment> crewAssignments,
      String shipImageAssetId,
      User user);

  // CrewMobilizationSchedule updateSchedule(String id, JsonNode patch);

}

package com.inlaco.crewmgrservice.feature.notify.application.port.in;

import com.inlaco.crewmgrservice.feature.crew.domain.model.CrewProfile;
import com.inlaco.crewmgrservice.feature.crewmobilization.domain.model.CrewMobilization;
import java.util.List;

public interface MobilizationNotificationUseCase {

  void notifyUsers(CrewMobilization schedule, List<CrewProfile> profiles);
}

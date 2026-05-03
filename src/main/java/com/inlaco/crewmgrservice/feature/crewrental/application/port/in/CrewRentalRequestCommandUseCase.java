package com.inlaco.crewmgrservice.feature.crewrental.application.port.in;

import com.inlaco.crewmgrservice.feature.crewrental.domain.model.CrewRentalRequest;
import com.inlaco.crewmgrservice.feature.user.domain.model.User;

public interface CrewRentalRequestCommandUseCase {
  void review(String requestId, boolean accepted, User reviewer);

  CrewRentalRequest create(
      CrewRentalRequest request, String detailFileAssetId, String shipImageAssetId, User user);

  CrewRentalRequest markSigning(CrewRentalRequest request, String contractId);
}

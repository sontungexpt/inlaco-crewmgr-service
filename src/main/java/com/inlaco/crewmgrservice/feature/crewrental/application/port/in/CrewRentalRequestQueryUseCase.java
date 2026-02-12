package com.inlaco.crewmgrservice.feature.crewrental.application.port.in;

import com.inlaco.crewmgrservice.feature.crewrental.application.model.CrewRentalRequestSearchCriteria;
import com.inlaco.crewmgrservice.feature.crewrental.domain.model.CrewRentalRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CrewRentalRequestQueryUseCase {

  CrewRentalRequest getRequest(String requestId);

  Page<CrewRentalRequest> getRequests(CrewRentalRequestSearchCriteria criteria, Pageable pageable);
}

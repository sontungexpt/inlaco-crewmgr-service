package com.inlaco.crewmgrservice.feature.crewhiring.service;

import com.inlaco.crewmgrservice.feature.crewhiring.dto.CrewRentalRequestFilter;
import com.inlaco.crewmgrservice.feature.crewhiring.model.CrewRentalRequest;
import com.inlaco.crewmgrservice.feature.user.model.User;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CrewRentalRequestService {

  CrewRentalRequest createRequest(CrewRentalRequest request);

  CrewRentalRequest getRequestById(String requestId);

  CrewRentalRequest saveRequest(CrewRentalRequest request);

  Page<CrewRentalRequest> findAllRequest(CrewRentalRequestFilter filter, Pageable pageable);

  void reviewRequest(String requestId, boolean accepted, User reviewer);

  Page<CrewRentalRequest> searchRequests(
      String query, CrewRentalRequestFilter filter, Pageable pageable);
}

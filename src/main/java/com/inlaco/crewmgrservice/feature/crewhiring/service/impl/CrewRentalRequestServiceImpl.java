package com.inlaco.crewmgrservice.feature.crewhiring.service.impl;

import com.inlaco.crewmgrservice.exceptions.ResourceNotFoundException;
import com.inlaco.crewmgrservice.feature.crewhiring.dto.CrewRentalRequestFilter;
import com.inlaco.crewmgrservice.feature.crewhiring.enums.CrewRentalRequestStatus;
import com.inlaco.crewmgrservice.feature.crewhiring.model.CrewRentalRequest;
import com.inlaco.crewmgrservice.feature.crewhiring.repository.CrewRentalRequestRepository;
import com.inlaco.crewmgrservice.feature.crewhiring.repository.CustomCrewRentalRequestRepository;
import com.inlaco.crewmgrservice.feature.crewhiring.service.CrewRentalRequestService;
import com.inlaco.crewmgrservice.feature.user.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CrewRentalRequestServiceImpl implements CrewRentalRequestService {

  private final CustomCrewRentalRequestRepository customCrewRentalRequestRepository;
  private final CrewRentalRequestRepository crewRentalRequestRepository;

  @Override
  public CrewRentalRequest createRequest(CrewRentalRequest request) {
    return crewRentalRequestRepository.save(request);
  }

  @Override
  public CrewRentalRequest getRequestById(String requestId) {
    return crewRentalRequestRepository
        .findById(requestId)
        .orElseThrow(() -> new ResourceNotFoundException(CrewRentalRequest.class, "id", requestId));
  }

  @Override
  public Page<CrewRentalRequest> searchRequests(
      String query, CrewRentalRequestFilter filter, Pageable pageable) {
    return customCrewRentalRequestRepository.searchRequests(query, filter, pageable);
  }

  @Override
  public void reviewRequest(String requestId, boolean accepted, User reviewer) {
    CrewRentalRequest request = getRequestById(requestId);
    request.setReviewedBy(new ObjectId(reviewer.getId()));
    request.setStatus(
        accepted ? CrewRentalRequestStatus.APPROVED : CrewRentalRequestStatus.REJECTED);
    crewRentalRequestRepository.save(request);
  }

  @Override
  public Page<CrewRentalRequest> findAllRequest(CrewRentalRequestFilter filter, Pageable pageable) {
    return customCrewRentalRequestRepository.findAllRequests(filter, pageable);
  }
}

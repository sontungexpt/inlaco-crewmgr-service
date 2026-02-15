package com.inlaco.crewmgrservice.feature.crewrental.application.service;

import com.inlaco.crewmgrservice.feature.crewrental.application.model.CrewRentalRequestSearchCriteria;
import com.inlaco.crewmgrservice.feature.crewrental.application.port.in.CrewRentalRequestQueryUseCase;
import com.inlaco.crewmgrservice.feature.crewrental.application.port.out.CrewRentalRequestRepository;
import com.inlaco.crewmgrservice.feature.crewrental.domain.model.CrewRentalRequest;
import com.inlaco.crewmgrservice.shared.kernel.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CrewRentalRequestQueryService implements CrewRentalRequestQueryUseCase {
  private final CrewRentalRequestRepository crewRentalRequestRepository;

  @Override
  public CrewRentalRequest getRequest(String requestId) {
    return crewRentalRequestRepository
        .findById(requestId)
        .orElseThrow(() -> new ResourceNotFoundException(CrewRentalRequest.class, "id", requestId));
  }

  @Override
  public Page<CrewRentalRequest> getRequests(
      CrewRentalRequestSearchCriteria criteria, Pageable pageable) {
    return crewRentalRequestRepository.findAll(criteria, pageable);
  }
}

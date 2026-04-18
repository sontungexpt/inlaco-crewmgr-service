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
    log.debug("Fetching crew rental request with ID: {}", requestId);
    return crewRentalRequestRepository
        .findById(requestId)
        .orElseThrow(
            () -> {
              log.warn("Crew rental request not found with ID: {}", requestId);
              return new ResourceNotFoundException(CrewRentalRequest.class, "id", requestId);
            });
  }

  @Override
  public Page<CrewRentalRequest> getRequests(
      CrewRentalRequestSearchCriteria criteria, Pageable pageable) {
    log.debug("Fetching crew rental requests with criteria: {}", criteria);
    return crewRentalRequestRepository.findAll(criteria, pageable);
  }
}

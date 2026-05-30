package com.inlaco.crewmgrservice.feature.crewmobilization.application.service;

import com.inlaco.crewmgrservice.feature.crew.application.port.out.CrewProfileRepository;
import com.inlaco.crewmgrservice.feature.crew.domain.model.CrewProfile;
import com.inlaco.crewmgrservice.feature.crewmobilization.application.mapper.CrewMobilizationDetailMapper;
import com.inlaco.crewmgrservice.feature.crewmobilization.application.model.AssignedCrewDetail;
import com.inlaco.crewmgrservice.feature.crewmobilization.application.model.CrewMobilizationDetail;
import com.inlaco.crewmgrservice.feature.crewmobilization.application.model.CrewMobilizationSearchCriteria;
import com.inlaco.crewmgrservice.feature.crewmobilization.application.port.in.CrewMobilizationQueryUseCase;
import com.inlaco.crewmgrservice.feature.crewmobilization.application.port.out.CrewMobilizationAssignmentRepository;
import com.inlaco.crewmgrservice.feature.crewmobilization.application.port.out.CrewMobilizationRepository;
import com.inlaco.crewmgrservice.feature.crewmobilization.domain.model.CrewMobilization;
import com.inlaco.crewmgrservice.feature.crewmobilization.domain.model.CrewMobilizationAssignment;
import com.inlaco.crewmgrservice.shared.kernel.exception.ResourceNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CrewMobilizationQueryService implements CrewMobilizationQueryUseCase {

  private final CrewMobilizationRepository crewMobilizationScheduleRepository;
  private final CrewProfileRepository crewProfileRepository;
  private final CrewMobilizationAssignmentRepository crewMobilizationAssignmentRepository;
  private final CrewMobilizationDetailMapper mapper;

  @Override
  public CrewMobilization findMobilization(String id) {
    log.debug("Fetching crew mobilization schedule with ID: {}", id);
    return crewMobilizationScheduleRepository
        .findById(id)
        .orElseThrow(
            () -> {
              log.warn("Crew mobilization schedule not found with ID: {}", id);
              return new ResourceNotFoundException(CrewMobilization.class, "id", id);
            });
  }

  @Override
  public CrewMobilizationDetail findDetailMobilization(String id) {
    log.info("Fetching detailed crew mobilization schedule with ID: {}", id);
    var mobilization = findMobilization(id);
    var crews = crewMobilizationAssignmentRepository.findByMobilizationId(id);

    if (crews == null || crews.isEmpty()) {
      return mapper.toDetail(mobilization, Collections.emptyList());
    }

    // Extract employeeCardIds
    List<String> employeeCardIds = new ArrayList<>(crews.size());
    for (CrewMobilizationAssignment ac : crews) {
      employeeCardIds.add(ac.getEmployeeCardId());
    }

    // Fetch profiles
    log.debug("Fetching crew profiles with employeeCardIds: {}", employeeCardIds);
    var profiles = crewProfileRepository.findAllByEmployeeCardId(employeeCardIds);

    // Build profile map
    Map<String, CrewProfile> profileMap = new HashMap<>(profiles.size());
    for (CrewProfile profile : profiles) {
      profileMap.put(profile.getEmployeeCardId(), profile);
    }

    // Build crew details
    log.debug("Building crew details");
    List<AssignedCrewDetail> crewDetails = new ArrayList<>(crews.size());

    for (CrewMobilizationAssignment ac : crews) {
      CrewProfile profile = profileMap.get(ac.getEmployeeCardId());

      AssignedCrewDetail detail = new AssignedCrewDetail();

      // schedule data
      detail.setEmployeeCardId(ac.getEmployeeCardId());
      detail.setRankOnBoard(ac.getRankOnBoard());
      detail.setStartDate(ac.getStartDate());
      detail.setEndDate(ac.getEndDate());
      detail.setRemark(ac.getRemark());

      // profile data
      if (profile != null) {
        detail.setId(profile.getId());
        detail.setAccountId(profile.getAccountId());
        detail.setFullName(profile.getFullName());
        detail.setEmail(profile.getEmail());
        detail.setPhoneNumber(profile.getPhoneNumber());
        detail.setAddress(profile.getAddress());
        detail.setGender(profile.getGender());

        if (profile.getProfessionalPosition() != null) {
          detail.setProfessionalPositions(
              Collections.singletonList(profile.getProfessionalPosition()));
        }
      }

      crewDetails.add(detail);
    }

    return mapper.toDetail(mobilization, crewDetails);
  }

  @Override
  public Page<CrewMobilization> findMobilizations(
      CrewMobilizationSearchCriteria criteria, Pageable pageable) {
    log.debug("Fetching crew mobilization schedules with criteria: {}", criteria);

    return crewMobilizationScheduleRepository.findAll(criteria, pageable);
  }

  @Override
  public List<CrewMobilizationAssignment> findCrewsAssigned(String mobilizationId) {
    return crewMobilizationAssignmentRepository.findByMobilizationId(mobilizationId);
  }

  @Override
  public List<CrewMobilizationAssignment> findAllActiveAssignments() {

    return crewMobilizationAssignmentRepository.findAllActiveAssignments();
  }

  @Override
  public Page<CrewMobilizationAssignment> findAllActiveAssignments(Pageable pageable) {
    throw new UnsupportedOperationException("Unimplemented method 'findAllActiveAssignments'");
  }

  @Override
  public List<CrewMobilizationAssignment> findAllActiveAssignmentsForClient(String clientId) {
    return crewMobilizationAssignmentRepository.findAllActiveAssignmentsForClient(clientId);
  }

  @Override
  public Page<CrewMobilizationAssignment> findAllActiveAssignmentsForClient(
      String clientId, Pageable pageable) {
    return crewMobilizationAssignmentRepository.findAllActiveAssignmentsForClient(
        clientId, pageable);
  }
}

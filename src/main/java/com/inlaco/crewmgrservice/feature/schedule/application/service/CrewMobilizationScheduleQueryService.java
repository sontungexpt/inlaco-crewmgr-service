package com.inlaco.crewmgrservice.feature.schedule.application.service;

import com.inlaco.crewmgrservice.feature.crew.application.port.out.CrewProfileRepository;
import com.inlaco.crewmgrservice.feature.crew.domain.model.CrewProfile;
import com.inlaco.crewmgrservice.feature.schedule.application.mapper.CrewMobilizationScheduleDetailMapper;
import com.inlaco.crewmgrservice.feature.schedule.application.model.AssignedCrewDetail;
import com.inlaco.crewmgrservice.feature.schedule.application.model.CrewMobilizationScheduleDetail;
import com.inlaco.crewmgrservice.feature.schedule.application.model.CrewMobilizationScheduleSearchCriteria;
import com.inlaco.crewmgrservice.feature.schedule.application.port.in.CrewMobilizationScheduleQueryUseCase;
import com.inlaco.crewmgrservice.feature.schedule.application.port.out.CrewMobilizationScheduleRepository;
import com.inlaco.crewmgrservice.feature.schedule.domain.model.AssignedCrew;
import com.inlaco.crewmgrservice.feature.schedule.domain.model.CrewMobilizationSchedule;
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
public class CrewMobilizationScheduleQueryService implements CrewMobilizationScheduleQueryUseCase {

  private final CrewMobilizationScheduleRepository crewMobilizationScheduleRepository;
  private final CrewProfileRepository crewProfileRepository;
  private final CrewMobilizationScheduleDetailMapper mapper;

  @Override
  public CrewMobilizationSchedule findSchedule(String id) {
    log.debug("Fetching crew mobilization schedule with ID: {}", id);
    return crewMobilizationScheduleRepository
        .findById(id)
        .orElseThrow(
            () -> {
              log.warn("Crew mobilization schedule not found with ID: {}", id);
              return new ResourceNotFoundException(CrewMobilizationSchedule.class, "id", id);
            });
  }

  @Override
  public CrewMobilizationScheduleDetail findDetailSchedule(String id) {
    log.info("Fetching detailed crew mobilization schedule with ID: {}", id);
    var schedule = findSchedule(id);
    var crews = schedule.getCrews();

    if (crews == null || crews.isEmpty()) {
      return mapper.toDetail(schedule, Collections.emptyList());
    }

    // Extract employeeCardIds
    List<String> employeeCardIds = new ArrayList<>(crews.size());
    for (AssignedCrew ac : crews) {
      employeeCardIds.add(ac.getEmployeeCardId());
    }

    // Fetch profiles
    var profiles = crewProfileRepository.findAllByEmployeeCardId(employeeCardIds);

    // Build profile map
    Map<String, CrewProfile> profileMap = new HashMap<>(profiles.size());
    for (CrewProfile profile : profiles) {
      profileMap.put(profile.getEmployeeCardId(), profile);
    }

    // Build crew details
    List<AssignedCrewDetail> crewDetails = new ArrayList<>(crews.size());

    for (AssignedCrew ac : crews) {
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

    return mapper.toDetail(schedule, crewDetails);
  }

  @Override
  public Page<CrewMobilizationSchedule> findSchedules(
      CrewMobilizationScheduleSearchCriteria criteria, Pageable pageable) {
    log.debug("Fetching crew mobilization schedules with criteria: {}", criteria);
    return crewMobilizationScheduleRepository.findAll(criteria, pageable);
  }
}

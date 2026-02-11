package com.inlaco.crewmgrservice.feature.schedule.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.inlaco.crewmgrservice.feature.crew.domain.model.CrewProfile;
import com.inlaco.crewmgrservice.feature.schedule.dto.MobilizationResponse;
import com.inlaco.crewmgrservice.feature.schedule.dto.SailorScheduleResponse;
import com.inlaco.crewmgrservice.feature.schedule.dto.ScheduleFilterable;
import com.inlaco.crewmgrservice.feature.schedule.event.NewAssignmentScheduleEvent;
import com.inlaco.crewmgrservice.feature.schedule.model.AssignedMobilization;
import com.inlaco.crewmgrservice.feature.schedule.repository.AssignmentScheduleRepository;
import com.inlaco.crewmgrservice.feature.schedule.repository.CustomScheduleRepository;
import com.inlaco.crewmgrservice.feature.schedule.service.ScheduleService;
import com.inlaco.crewmgrservice.utils.JsonMergePatchUtils;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScheduleServiceImpl implements ScheduleService {

  private final AssignmentScheduleRepository scheduleRepository;
  private final CustomScheduleRepository customScheduleRepository;
  private final JsonMergePatchUtils jsonMergePatch;
  private final ApplicationEventPublisher eventPublisher;

  @Override
  public AssignedMobilization createSchedule(AssignedMobilization schedule) {
    var newSchedule = scheduleRepository.save(schedule);
    log.info(
        "Schedule created successfully [id={}, startDate={}, endDate={}]",
        newSchedule.getId(),
        newSchedule.getStartDate(),
        newSchedule.getEndDate());
    eventPublisher.publishEvent(new NewAssignmentScheduleEvent(this, newSchedule));
    return newSchedule;
  }

  @Override
  public Page<AssignedMobilization> findPaginationSchedules(
      ScheduleFilterable filterable, Pageable pageable) {
    return customScheduleRepository.findAllSchedules(filterable, pageable);
  }

  @Override
  public List<AssignedMobilization> findSchedules(ScheduleFilterable filterable) {
    return customScheduleRepository.findAllSchedules(filterable);
  }

  @Override
  public Page<SailorScheduleResponse> findPaginationSchedulesByCardId(
      String cardId, ScheduleFilterable filterable, Pageable pageable) {
    // var profile = sailorService.findSailorProfileByCardId(cardId);
    throw new UnsupportedOperationException();
    // return customScheduleRepository
    //     .findPaginationSchedulesByCardId(cardId, filterable, pageable)
    //     .map(it -> toSailorScheduleResponse(it, cardId, profile));
  }

  private SailorScheduleResponse toSailorScheduleResponse(
      AssignedMobilization schedule, String cardId, CrewProfile profile) {
    return SailorScheduleResponse.builder()
        .startDate(schedule.getStartDate())
        .endDate(schedule.getEndDate())
        .professionalPosition(profile.getProfessionalPosition())
        // schedule.getCrewMembers().stream()
        //     .filter(crew -> crew.getCardId().equals(cardId))
        //     .toList()
        //     .get(0)
        //     .getProfessionalPosition())
        .cardId(cardId)
        .detail(schedule)
        .build();
  }

  @Override
  public List<SailorScheduleResponse> findSchedulesByCardId(
      String cardId, ScheduleFilterable filterable) {
    // var profile = sailorService.findSailorProfileByCardId(cardId);
    // return customScheduleRepository.findSchedulesByCardId(cardId, filterable).stream()
    //     .map(it -> toSailorScheduleResponse(it, cardId, profile))
    //     .toList();
    throw new UnsupportedOperationException();
  }

  @Override
  public MobilizationResponse findDetailScheduleById(String id) {
    return customScheduleRepository.findDetailSchedule(id);
  }

  @Override
  public MobilizationResponse updateSchedule(String id, JsonNode patch) {
    jsonMergePatch.patch(id, AssignedMobilization.class, patch);
    return customScheduleRepository.findDetailSchedule(id);
  }
}

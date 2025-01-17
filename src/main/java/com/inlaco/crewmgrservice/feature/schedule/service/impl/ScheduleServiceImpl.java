package com.inlaco.crewmgrservice.feature.schedule.service.impl;

import com.inlaco.crewmgrservice.feature.schedule.dto.SailorScheduleResponse;
import com.inlaco.crewmgrservice.feature.schedule.dto.ScheduleFilterable;
import com.inlaco.crewmgrservice.feature.schedule.dto.ScheduleResponse;
import com.inlaco.crewmgrservice.feature.schedule.model.AssigmentSchedule;
import com.inlaco.crewmgrservice.feature.schedule.repository.AssignmentScheduleRepository;
import com.inlaco.crewmgrservice.feature.schedule.repository.CustomScheduleRepository;
import com.inlaco.crewmgrservice.feature.schedule.service.ScheduleService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScheduleServiceImpl implements ScheduleService {

  private final AssignmentScheduleRepository scheduleRepository;
  private final CustomScheduleRepository customScheduleRepository;

  @Override
  public AssigmentSchedule createSchedule(AssigmentSchedule schedule) {
    return scheduleRepository.save(schedule);
  }

  @Override
  public Page<AssigmentSchedule> findPaginationSchedules(
      ScheduleFilterable filterable, Pageable pageable) {
    return customScheduleRepository.findAllSchedules(filterable, pageable);
  }

  @Override
  public List<AssigmentSchedule> findSchedules(ScheduleFilterable filterable) {
    return customScheduleRepository.findAllSchedules(filterable);
  }

  @Override
  public Page<SailorScheduleResponse> findPaginationSchedulesByCardId(
      String cardId, ScheduleFilterable filterable, Pageable pageable) {
    return customScheduleRepository
        .findPaginationSchedulesByCardId(cardId, filterable, pageable)
        .map(it -> toSailorScheduleResponse(it, cardId));
  }

  private SailorScheduleResponse toSailorScheduleResponse(
      AssigmentSchedule schedule, String cardId) {
    return SailorScheduleResponse.builder()
        .startDate(schedule.getStartDate())
        .estimatedEndDate(schedule.getEstimatedEndDate())
        .professionalPosition(
            schedule.getCrewMembers().stream()
                .filter(crew -> crew.getCardId().equals(cardId))
                .toList()
                .get(-1)
                .getProfessionalPosition())
        .cardId(cardId)
        .detail(schedule)
        .build();
  }

  @Override
  public List<SailorScheduleResponse> findSchedulesByCardId(
      String cardId, ScheduleFilterable filterable) {
    return customScheduleRepository.findSchedulesByCardId(cardId, filterable).stream()
        .map(it -> toSailorScheduleResponse(it, cardId))
        .toList();
  }

  @Override
  public ScheduleResponse findDetailScheduleById(String id) {
    return customScheduleRepository.findDetailSchedule(id);
  }
}

package com.inlaco.crewmgrservice.feature.schedule.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.inlaco.crewmgrservice.feature.schedule.dto.MobilizationResponse;
import com.inlaco.crewmgrservice.feature.schedule.dto.SailorScheduleResponse;
import com.inlaco.crewmgrservice.feature.schedule.dto.ScheduleFilterable;
import com.inlaco.crewmgrservice.feature.schedule.model.AssignedMobilization;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ScheduleService {

  AssignedMobilization createSchedule(AssignedMobilization schedule);

  MobilizationResponse updateSchedule(String id, JsonNode patch);

  MobilizationResponse findDetailScheduleById(String id);

  Page<AssignedMobilization> findPaginationSchedules(
      ScheduleFilterable filterable, Pageable pageable);

  List<AssignedMobilization> findSchedules(ScheduleFilterable filterable);

  Page<SailorScheduleResponse> findPaginationSchedulesByCardId(
      String cardId, ScheduleFilterable filterable, Pageable pageable);

  List<SailorScheduleResponse> findSchedulesByCardId(String cardId, ScheduleFilterable filterable);
}

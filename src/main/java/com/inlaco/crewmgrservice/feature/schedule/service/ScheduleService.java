package com.inlaco.crewmgrservice.feature.schedule.service;

import com.inlaco.crewmgrservice.feature.schedule.dto.SailorScheduleResponse;
import com.inlaco.crewmgrservice.feature.schedule.dto.ScheduleFilterable;
import com.inlaco.crewmgrservice.feature.schedule.dto.ScheduleResponse;
import com.inlaco.crewmgrservice.feature.schedule.model.AssigmentSchedule;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ScheduleService {

  AssigmentSchedule createSchedule(AssigmentSchedule schedule);

  ScheduleResponse findDetailScheduleById(String id);

  Page<AssigmentSchedule> findPaginationSchedules(ScheduleFilterable filterable, Pageable pageable);

  List<AssigmentSchedule> findSchedules(ScheduleFilterable filterable);

  Page<SailorScheduleResponse> findPaginationSchedulesByCardId(
      String cardId, ScheduleFilterable filterable, Pageable pageable);

  List<SailorScheduleResponse> findSchedulesByCardId(String cardId, ScheduleFilterable filterable);
}

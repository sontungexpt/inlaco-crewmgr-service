package com.inlaco.crewmgrservice.feature.schedule.application.port.in;

import com.inlaco.crewmgrservice.feature.schedule.application.model.CrewMobilizationScheduleDetail;
import com.inlaco.crewmgrservice.feature.schedule.application.model.CrewMobilizationScheduleSearchCriteria;
import com.inlaco.crewmgrservice.feature.schedule.domain.model.CrewMobilizationSchedule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CrewMobilizationScheduleQueryUseCase {

  CrewMobilizationSchedule findSchedule(String id);

  CrewMobilizationScheduleDetail findDetailSchedule(String id);

  Page<CrewMobilizationSchedule> findSchedules(
      CrewMobilizationScheduleSearchCriteria criteria, Pageable pageable);

  // List<AssignedMobilization> findSchedules(ScheduleFilterable filterable);

  // Page<SailorScheduleResponse> findPaginationSchedulesByCardId(
  //     String cardId, ScheduleFilterable filterable, Pageable pageable);

  // List<SailorScheduleResponse> findSchedulesByCardId(String cardId, ScheduleFilterable
  // filterable);
}

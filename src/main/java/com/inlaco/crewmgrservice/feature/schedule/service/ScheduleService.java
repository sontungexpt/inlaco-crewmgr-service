// package com.inlaco.crewmgrservice.feature.schedule.service;
// import com.fasterxml.jackson.databind.JsonNode;
// import com.inlaco.crewmgrservice.feature.schedule.domain.model.AssignedMobilization;
// import
// com.inlaco.crewmgrservice.feature.schedule.presentation.rest.dto.CrewMobilizationScheduleResponse;
// import com.inlaco.crewmgrservice.feature.schedule.presentation.rest.dto.ScheduleFilterable;
// import
// com.inlaco.crewmgrservice.feature.schedule.presentation.rest.dto.response.SailorScheduleResponse;

// import java.util.List;
// import org.springframework.data.domain.Page;
// import org.springframework.data.domain.Pageable;

// public interface ScheduleService {

//   AssignedMobilization createSchedule(AssignedMobilization schedule);

//   CrewMobilizationScheduleResponse updateSchedule(String id, JsonNode patch);

//   CrewMobilizationScheduleResponse findDetailScheduleById(String id);

//   Page<AssignedMobilization> findPaginationSchedules(
//       ScheduleFilterable filterable, Pageable pageable);

//   List<AssignedMobilization> findSchedules(ScheduleFilterable filterable);

//   Page<SailorScheduleResponse> findPaginationSchedulesByCardId(
//       String cardId, ScheduleFilterable filterable, Pageable pageable);

//   List<SailorScheduleResponse> findSchedulesByCardId(String cardId, ScheduleFilterable
// filterable);
// }

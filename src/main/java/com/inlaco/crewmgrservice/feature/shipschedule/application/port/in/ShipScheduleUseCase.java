package com.inlaco.crewmgrservice.feature.shipschedule.application.port.in;

import com.inlaco.crewmgrservice.feature.shipschedule.application.model.ShipScheduleDetail;
import com.inlaco.crewmgrservice.feature.shipschedule.application.model.ShipScheduleSearchCriteria;
import com.inlaco.crewmgrservice.feature.shipschedule.domain.model.ShipSchedule;
import com.inlaco.crewmgrservice.feature.shipschedule.domain.model.ShipScheduleCrewAssignment;
import com.inlaco.crewmgrservice.feature.user.domain.model.User;
import java.time.Instant;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ShipScheduleUseCase {
  ShipSchedule createSchedule(
      ShipSchedule schedule, List<ShipScheduleCrewAssignment> assignments, User authenticatedUser);

  Page<ShipSchedule> getSchedules(ShipScheduleSearchCriteria criteria, Pageable pageable);

  ShipSchedule getSchedule(String scheduleId);

  ShipScheduleDetail getScheduleDetail(String scheduleId);

  List<ShipScheduleCrewAssignment> findAssignmentsOverlappingTimeRange(
      String profileId, Instant startDate, Instant endDate);

  boolean hasAssignmentOverlap(String profileId, Instant startDate, Instant endDate);

  boolean hasAssignmentOverlapByEmployeeCardId(
      String employeeCardId, Instant startDate, Instant endDate);

  List<ShipScheduleCrewAssignment> findAssignmentsFullyWithinTimeRange(
      String profileId, Instant startDate, Instant endDate);

  List<ShipScheduleCrewAssignment> findAssignmentsTimeRangeOverlap(
      Instant startDate, Instant endDate);
}

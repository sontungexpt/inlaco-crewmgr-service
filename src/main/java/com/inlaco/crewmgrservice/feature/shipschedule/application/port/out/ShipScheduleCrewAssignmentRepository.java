package com.inlaco.crewmgrservice.feature.shipschedule.application.port.out;

import com.inlaco.crewmgrservice.feature.shipschedule.domain.model.ShipScheduleCrewAssignment;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface ShipScheduleCrewAssignmentRepository {

  ShipScheduleCrewAssignment save(ShipScheduleCrewAssignment shipScheduleCrewAssignment);

  List<ShipScheduleCrewAssignment> saveAll(
      Iterable<ShipScheduleCrewAssignment> shipScheduleCrewAssignments);

  List<ShipScheduleCrewAssignment> findByScheduleId(String shipScheduleId);

  Optional<ShipScheduleCrewAssignment> findByAccountIdAndScheduleId(
      String accountId, String shipScheduleId);

  /*
   * Find all crew assignments that have ANY time overlap
   * with the requested timetable range.
   *
   * The assignment does NOT need to be fully inside the range.
   * It is included as long as at least one part overlaps.
   *
   * Overlap condition:
   *
   * assignment.boardingTime <= requestedEnd
   * AND
   * assignment.disembarkTime >= requestedStart
   *
   * Examples:
   *
   * Assignment: 01 -> 10
   * Request:    05 -> 15
   * Result: INCLUDED
   *
   * Assignment: 05 -> 15
   * Request:    01 -> 10
   * Result: INCLUDED
   *
   * Assignment: 01 -> 30
   * Request:    10 -> 15
   * Result: INCLUDED
   *
   * Assignment: 01 -> 05
   * Request:    06 -> 10
   * Result: NOT INCLUDED
   */
  List<ShipScheduleCrewAssignment> findByProfileIdAndTimeRangeOverlap(
      String profileId, Instant startDate, Instant endDate);

  boolean existsProfileIdAndTimeRangeOverlap(String profileId, Instant startDate, Instant endDate);

  List<ShipScheduleCrewAssignment> findByTimeRangeOverlap(Instant startTime, Instant endTime);

  /*
   * Find all assignments that are COMPLETELY inside
   * the requested time range.
   *
   * This means:
   *
   * - The assignment starts AFTER or AT queryStart
   * - The assignment ends BEFORE or AT queryEnd
   *
   * Condition:
   *
   * queryStart <= boardingTime
   * AND
   * disembarkTime <= queryEnd
   *
   * Example:
   *
   * Assignment: 05 -> 10
   * Range:      01 -> 15  => INCLUDED (fully inside)
   *
   * Assignment: 01 -> 20
   * Range:      05 -> 15  => NOT INCLUDED (extends outside)
   */

  List<ShipScheduleCrewAssignment> findByProfileIdFullyInTimeRange(
      String profileId, Instant startDate, Instant endDate);
}

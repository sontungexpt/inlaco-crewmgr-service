package com.inlaco.crewmgrservice.feature.shipschedule.application.port.out;

import com.inlaco.crewmgrservice.feature.shipschedule.domain.model.ShipScheduleCrewAssignment;
import java.util.List;
import java.util.Optional;

public interface ShipScheduleCrewAssignmentRepository {

  ShipScheduleCrewAssignment save(ShipScheduleCrewAssignment shipScheduleCrewAssignment);

  List<ShipScheduleCrewAssignment> saveAll(
      Iterable<ShipScheduleCrewAssignment> shipScheduleCrewAssignments);

  List<ShipScheduleCrewAssignment> findByScheduleId(String shipScheduleId);

  Optional<ShipScheduleCrewAssignment> findByAccountIdAndScheduleId(
      String accountId, String shipScheduleId);
}

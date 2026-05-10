package com.inlaco.crewmgrservice.feature.shipschedule.application.port.out;

import com.inlaco.crewmgrservice.feature.shipschedule.domain.model.ShipScheduleCrewAssignment;
import java.util.List;
import java.util.Optional;

public interface ShipScheduleCrewAssignmentRepository {

  ShipScheduleCrewAssignment save(ShipScheduleCrewAssignment shipScheduleCrewAssignment);

  List<ShipScheduleCrewAssignment> findByScheduleId(String shipScheduleId);

  List<ShipScheduleCrewAssignment> findByProfileId(String crewId);

  Optional<ShipScheduleCrewAssignment> findByAccountIdAndScheduleId(
      String accountId, String shipScheduleId);

  boolean existsByCrewIdAndShipScheduleId(String crewId, String shipScheduleId);
}

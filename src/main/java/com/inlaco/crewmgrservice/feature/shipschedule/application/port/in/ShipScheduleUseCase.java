package com.inlaco.crewmgrservice.feature.shipschedule.application.port.in;

import com.inlaco.crewmgrservice.feature.shipschedule.domain.model.ShipSchedule;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface ShipScheduleUseCase {
  ShipSchedule createSchedule(ShipSchedule schedule);
  ShipSchedule updateSchedule(String id, ShipSchedule schedule);
  void deleteSchedule(String id);
  Optional<ShipSchedule> getScheduleById(String id);
  List<ShipSchedule> getSchedulesByClientId(String clientId);
  List<ShipSchedule> getSchedulesByShipImo(String shipImo);
  List<ShipSchedule> getSchedulesByDateRange(Instant startTime, Instant endTime);
  List<ShipSchedule> getAllSchedules();
  ShipSchedule addCrewMember(String scheduleId, String employeeCardId);
  ShipSchedule removeCrewMember(String scheduleId, String employeeCardId);
  ShipSchedule updateCrewList(String scheduleId, List<String> employeeCardIds);
}

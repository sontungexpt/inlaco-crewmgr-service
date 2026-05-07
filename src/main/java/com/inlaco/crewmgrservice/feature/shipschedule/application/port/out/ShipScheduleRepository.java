package com.inlaco.crewmgrservice.feature.shipschedule.application.port.out;

import com.inlaco.crewmgrservice.feature.shipschedule.domain.model.ShipSchedule;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface ShipScheduleRepository {
  ShipSchedule save(ShipSchedule shipSchedule);
  Optional<ShipSchedule> findById(String id);
  List<ShipSchedule> findByClientId(String clientId);
  List<ShipSchedule> findByShipImo(String shipImo);
  List<ShipSchedule> findByDepartureTimeBetween(Instant startTime, Instant endTime);
  List<ShipSchedule> findByStatus(String status);
  List<ShipSchedule> findAll();
  void deleteById(String id);
  boolean existsById(String id);
}

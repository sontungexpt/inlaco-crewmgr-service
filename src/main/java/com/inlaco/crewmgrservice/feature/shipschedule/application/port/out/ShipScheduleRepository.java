package com.inlaco.crewmgrservice.feature.shipschedule.application.port.out;

import com.inlaco.crewmgrservice.feature.shipschedule.application.model.ShipScheduleSearchCriteria;
import com.inlaco.crewmgrservice.feature.shipschedule.domain.model.ShipSchedule;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ShipScheduleRepository {
  ShipSchedule save(ShipSchedule shipSchedule);

  Optional<ShipSchedule> findById(String id);

  Page<ShipSchedule> findAll(Pageable pageable);

  Page<ShipSchedule> findAll(ShipScheduleSearchCriteria criteria, Pageable pageable);

  List<ShipSchedule> findByVesselOwnerId(String vesselOwnerId);

  List<ShipSchedule> findByShipImo(String shipImo);

  List<ShipSchedule> findByShipImoAndTimeOverlap(
      String shipImo, Instant startTime, Instant endTime);

  Optional<ShipSchedule> findOneByShipImoAndTimeOverlap(
      String shipImo, Instant startTime, Instant endTime);

  List<ShipSchedule> findByDepartureTimeBetween(Instant startTime, Instant endTime);

  List<ShipSchedule> findByStatus(String status);
}

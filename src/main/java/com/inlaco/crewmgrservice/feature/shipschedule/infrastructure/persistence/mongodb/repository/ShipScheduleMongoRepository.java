package com.inlaco.crewmgrservice.feature.shipschedule.infrastructure.persistence.mongodb.repository;

import com.inlaco.crewmgrservice.feature.shipschedule.infrastructure.persistence.mongodb.entity.ShipScheduleEntity;
import java.time.Instant;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShipScheduleMongoRepository extends MongoRepository<ShipScheduleEntity, String> {
  Page<ShipScheduleEntity> findByClientId(String clientId, Pageable pageable);

  List<ShipScheduleEntity> findByClientId(String clientId);

  Page<ShipScheduleEntity> findByShipIMO(String shipImo, Pageable pageable);

  List<ShipScheduleEntity> findByShipIMO(String shipImo);

  Page<ShipScheduleEntity> findByDepartureTimeBetween(
      Instant startTime, Instant endTime, Pageable pageable);

  List<ShipScheduleEntity> findByDepartureTimeBetween(Instant startTime, Instant endTime);

  Page<ShipScheduleEntity> findByStatus(String status, Pageable pageable);

  List<ShipScheduleEntity> findByStatus(String status);
}

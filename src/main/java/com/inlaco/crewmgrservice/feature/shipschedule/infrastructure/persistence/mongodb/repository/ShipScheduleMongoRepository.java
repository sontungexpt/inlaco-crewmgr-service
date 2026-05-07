package com.inlaco.crewmgrservice.feature.shipschedule.infrastructure.persistence.mongodb.repository;

import com.inlaco.crewmgrservice.feature.shipschedule.infrastructure.persistence.mongodb.entity.ShipScheduleEntity;
import java.time.Instant;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ShipScheduleMongoRepository extends MongoRepository<ShipScheduleEntity, String> {
  List<ShipScheduleEntity> findByClientId(String clientId);
  List<ShipScheduleEntity> findByShipImo(String shipImo);
  List<ShipScheduleEntity> findByDepartureTimeBetween(Instant startTime, Instant endTime);
  List<ShipScheduleEntity> findByStatus(String status);
  
  @Query("{ 'employeeCardIds': { $in: ?0 } }")
  List<ShipScheduleEntity> findByEmployeeCardIds(List<String> employeeCardIds);
}

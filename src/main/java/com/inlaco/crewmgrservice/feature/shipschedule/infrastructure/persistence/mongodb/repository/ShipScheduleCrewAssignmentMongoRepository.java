package com.inlaco.crewmgrservice.feature.shipschedule.infrastructure.persistence.mongodb.repository;

import com.inlaco.crewmgrservice.feature.shipschedule.infrastructure.persistence.mongodb.entity.ShipScheduleCrewAssignmentEntity;
import java.util.List;
import java.util.Optional;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShipScheduleCrewAssignmentMongoRepository
    extends MongoRepository<ShipScheduleCrewAssignmentEntity, String> {

  Page<ShipScheduleCrewAssignmentEntity> findByShipIMO(String shipImo, Pageable pageable);

  List<ShipScheduleCrewAssignmentEntity> findByShipIMO(String shipImo);

  List<ShipScheduleCrewAssignmentEntity> findByScheduleId(ObjectId scheduleId);

  Page<ShipScheduleCrewAssignmentEntity> findByAccountId(ObjectId accountId, Pageable pageable);

  List<ShipScheduleCrewAssignmentEntity> findByAccountId(ObjectId accountId);

  Optional<ShipScheduleCrewAssignmentEntity> findByAccountIdAndScheduleId(
      ObjectId accountId, ObjectId scheduleId);
}

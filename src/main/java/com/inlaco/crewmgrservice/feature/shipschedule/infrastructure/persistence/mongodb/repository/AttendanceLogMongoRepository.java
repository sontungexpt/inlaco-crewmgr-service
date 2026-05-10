package com.inlaco.crewmgrservice.feature.shipschedule.infrastructure.persistence.mongodb.repository;

import com.inlaco.crewmgrservice.feature.shipschedule.infrastructure.persistence.mongodb.entity.AttendanceLogEntity;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AttendanceLogMongoRepository extends MongoRepository<AttendanceLogEntity, String> {

  List<AttendanceLogEntity> findByCrewAccountId(String crewAccountId);

  List<AttendanceLogEntity> findByShipScheduleId(String shipScheduleId);

  List<AttendanceLogEntity> findByCrewAccountIdAndShipScheduleId(
      String crewAccountId, String shipScheduleId);

  List<AttendanceLogEntity> findByTimestampBetween(Instant startTime, Instant endTime);

  List<AttendanceLogEntity> findByCrewAccountIdAndTimestampBetween(
      String crewAccountId, Instant startTime, Instant endTime);

  List<AttendanceLogEntity> findByShipScheduleIdAndTimestampBetween(
      String shipScheduleId, Instant startTime, Instant endTime);

  Optional<AttendanceLogEntity> findTopByCrewEmployeeCardIdAndShipScheduleIdOrderByTimestampDesc(
      String employeeCardId, String shipScheduleId);

  Page<AttendanceLogEntity> findAll(Pageable pageable);
}

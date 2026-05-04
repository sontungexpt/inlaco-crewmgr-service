package com.inlaco.crewmgrservice.feature.attendance.infrastructure.persistence.mongodb.repository;

import com.inlaco.crewmgrservice.feature.attendance.domain.enums.CheckType;
import com.inlaco.crewmgrservice.feature.attendance.domain.model.AttendanceLog;
import com.inlaco.crewmgrservice.feature.attendance.infrastructure.persistence.mongodb.entity.AttendanceLogEntity;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttendanceLogMongoRepository extends MongoRepository<AttendanceLogEntity, String> {

  Optional<AttendanceLog> findTopByScheduleIdAndPersonIdAndCheckTypeOrderByTimestampDesc(
      String scheduleId, String personId, CheckType checkType);
}

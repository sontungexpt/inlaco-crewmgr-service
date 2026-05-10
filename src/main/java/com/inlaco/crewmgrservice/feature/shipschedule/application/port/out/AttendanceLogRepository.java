package com.inlaco.crewmgrservice.feature.shipschedule.application.port.out;

import com.inlaco.crewmgrservice.feature.shipschedule.domain.model.AttendanceLog;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AttendanceLogRepository {
  AttendanceLog save(AttendanceLog attendanceLog);

  Optional<AttendanceLog> findById(String id);

  Optional<AttendanceLog> findLatestByEmployeeCardIdAndShipScheduleId(
      String employeeCardId, String shipScheduleId);

  List<AttendanceLog> findByCrewId(String crewId);

  List<AttendanceLog> findByShipScheduleId(String shipScheduleId);

  List<AttendanceLog> findByCrewIdAndShipScheduleId(String crewId, String shipScheduleId);

  List<AttendanceLog> findByTimestampBetween(Instant startTime, Instant endTime);

  List<AttendanceLog> findByCrewIdAndTimestampBetween(
      String crewId, Instant startTime, Instant endTime);

  List<AttendanceLog> findByShipScheduleIdAndTimestampBetween(
      String shipScheduleId, Instant startTime, Instant endTime);

  Page<AttendanceLog> findAll(Pageable pageable);

  void deleteById(String id);

  boolean existsById(String id);
}

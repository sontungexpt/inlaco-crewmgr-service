package com.inlaco.crewmgrservice.feature.attendance.application.port.out;

import com.inlaco.crewmgrservice.feature.attendance.domain.model.AttendanceLog;
import java.util.Optional;

public interface AttendanceRepository {

  AttendanceLog save(AttendanceLog attendance);

  Optional<AttendanceLog> findByUserIdAndScheduleId(String userId, String scheduleId);

  boolean existsByUserIdAndScheduleId(String userId, String scheduleId);
}

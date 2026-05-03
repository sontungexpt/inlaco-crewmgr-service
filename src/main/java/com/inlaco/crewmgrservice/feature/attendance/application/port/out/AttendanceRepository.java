package com.inlaco.crewmgrservice.feature.attendance.application.port.out;

import com.inlaco.crewmgrservice.feature.attendance.domain.model.Attendance;
import java.util.Optional;

public interface AttendanceRepository {

  Attendance save(Attendance attendance);

  Optional<Attendance> findByUserIdAndScheduleId(String userId, String scheduleId);

  boolean existsByUserIdAndScheduleId(String userId, String scheduleId);
}

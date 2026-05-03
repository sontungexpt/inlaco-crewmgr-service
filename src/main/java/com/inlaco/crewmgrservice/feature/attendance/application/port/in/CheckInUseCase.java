package com.inlaco.crewmgrservice.feature.attendance.application.port.in;

import com.inlaco.crewmgrservice.feature.attendance.application.dto.CheckInRequest;
import com.inlaco.crewmgrservice.feature.attendance.domain.model.Attendance;

public interface CheckInUseCase {

  Attendance checkIn(String userId, CheckInRequest request);
}

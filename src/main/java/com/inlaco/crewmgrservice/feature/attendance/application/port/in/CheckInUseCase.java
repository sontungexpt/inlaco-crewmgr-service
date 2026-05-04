package com.inlaco.crewmgrservice.feature.attendance.application.port.in;

import com.inlaco.crewmgrservice.feature.attendance.application.dto.CheckInCommand;
import com.inlaco.crewmgrservice.feature.attendance.domain.model.AttendanceLog;

public interface CheckInUseCase {

  AttendanceLog checkIn(CheckInCommand request);
}

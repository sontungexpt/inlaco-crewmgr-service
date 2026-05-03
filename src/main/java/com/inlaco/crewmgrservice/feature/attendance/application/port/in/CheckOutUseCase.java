package com.inlaco.crewmgrservice.feature.attendance.application.port.in;

import com.inlaco.crewmgrservice.feature.attendance.application.dto.CheckOutRequest;
import com.inlaco.crewmgrservice.feature.attendance.domain.model.Attendance;

public interface CheckOutUseCase {

  Attendance checkOut(String userId, CheckOutRequest request);
}

package com.inlaco.crewmgrservice.feature.shipschedule.application.port.in;

import com.inlaco.crewmgrservice.feature.shipschedule.application.model.AttendanceLogSearchCriteria;
import com.inlaco.crewmgrservice.feature.shipschedule.domain.model.AttendanceLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AttendanceLogUseCase {

  Page<AttendanceLog> getLogs(AttendanceLogSearchCriteria criteria, Pageable pageable);
}

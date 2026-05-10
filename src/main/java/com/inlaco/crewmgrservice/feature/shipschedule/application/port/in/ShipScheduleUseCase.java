package com.inlaco.crewmgrservice.feature.shipschedule.application.port.in;

import com.inlaco.crewmgrservice.feature.shipschedule.application.model.ShipScheduleDetail;
import com.inlaco.crewmgrservice.feature.shipschedule.application.model.ShipScheduleSearchCriteria;
import com.inlaco.crewmgrservice.feature.shipschedule.domain.model.ShipSchedule;
import com.inlaco.crewmgrservice.feature.shipschedule.domain.model.ShipScheduleCrewAssignment;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ShipScheduleUseCase {
  ShipSchedule createSchedule(ShipSchedule schedule, List<ShipScheduleCrewAssignment> assignments);

  Page<ShipSchedule> getSchedules(ShipScheduleSearchCriteria criteria, Pageable pageable);

  ShipSchedule getSchedule(String scheduleId);

  ShipScheduleDetail getScheduleDetail(String scheduleId);
}

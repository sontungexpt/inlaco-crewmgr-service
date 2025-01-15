package com.inlaco.crewmgrservice.feature.schedule.service;

import com.inlaco.crewmgrservice.feature.schedule.dto.SailorWorkScheduleResponse;
import com.inlaco.crewmgrservice.feature.schedule.model.MasterAssignmentSchedule;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ScheduleService {

  Page<SailorWorkScheduleResponse> getScheduleBySailorId(String sailorId, Pageable pageable);

  MasterAssignmentSchedule createSchedule(
      MasterAssignmentSchedule masterAssignmentSchedule, List<String> sailorIds);
}

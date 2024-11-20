package com.inlaco.crewmgrservice.feature.schedule.service;

import com.inlaco.crewmgrservice.feature.schedule.dto.SailorWorkScheduleResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ScheduleService {

  Page<SailorWorkScheduleResponse> getScheduleBySailorId(String sailorId, Pageable pageable);
}

package com.inlaco.crewmgrservice.feature.crewmobilization.application.port.in;

import com.inlaco.crewmgrservice.feature.crewmobilization.application.model.CrewMobilizationDetail;
import com.inlaco.crewmgrservice.feature.crewmobilization.application.model.CrewMobilizationSearchCriteria;
import com.inlaco.crewmgrservice.feature.crewmobilization.domain.model.CrewMobilizationSchedule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CrewMobilizationQueryUseCase {

  CrewMobilizationSchedule findSchedule(String id);

  CrewMobilizationDetail findDetailSchedule(String id);

  Page<CrewMobilizationSchedule> findSchedules(
      CrewMobilizationSearchCriteria criteria, Pageable pageable);
}

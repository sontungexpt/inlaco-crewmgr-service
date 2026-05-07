package com.inlaco.crewmgrservice.feature.crewmobilization.application.port.in;

import com.inlaco.crewmgrservice.feature.crewmobilization.application.model.CrewMobilizationDetail;
import com.inlaco.crewmgrservice.feature.crewmobilization.application.model.CrewMobilizationSearchCriteria;
import com.inlaco.crewmgrservice.feature.crewmobilization.domain.model.CrewMobilization;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CrewMobilizationQueryUseCase {

  CrewMobilization findSchedule(String id);

  CrewMobilizationDetail findDetailSchedule(String id);

  Page<CrewMobilization> findSchedules(CrewMobilizationSearchCriteria criteria, Pageable pageable);
}

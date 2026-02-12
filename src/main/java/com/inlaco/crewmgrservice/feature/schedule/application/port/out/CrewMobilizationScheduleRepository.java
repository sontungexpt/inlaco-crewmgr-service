package com.inlaco.crewmgrservice.feature.schedule.application.port.out;

import com.inlaco.crewmgrservice.feature.schedule.application.model.CrewMobilizationScheduleSearchCriteria;
import com.inlaco.crewmgrservice.feature.schedule.domain.model.CrewMobilizationSchedule;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CrewMobilizationScheduleRepository {

  CrewMobilizationSchedule save(CrewMobilizationSchedule schedule);

  Optional<CrewMobilizationSchedule> findById(String id);

  Page<CrewMobilizationSchedule> findAll(Pageable pageable);

  Page<CrewMobilizationSchedule> findAll(
      CrewMobilizationScheduleSearchCriteria criteria, Pageable pageable);

  // List<AssignedMobilization> findByCrewMembersCardIdContains(String cardId);
}

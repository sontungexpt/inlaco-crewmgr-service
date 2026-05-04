package com.inlaco.crewmgrservice.feature.crewmobilization.application.port.out;

import com.inlaco.crewmgrservice.feature.crewmobilization.application.model.CrewMobilizationSearchCriteria;
import com.inlaco.crewmgrservice.feature.crewmobilization.domain.model.CrewMobilizationSchedule;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CrewMobilizationRepository {

  CrewMobilizationSchedule save(CrewMobilizationSchedule schedule);

  Optional<CrewMobilizationSchedule> findById(String id);

  Page<CrewMobilizationSchedule> findAll(Pageable pageable);

  Page<CrewMobilizationSchedule> findAll(
      CrewMobilizationSearchCriteria criteria, Pageable pageable);

  // List<AssignedMobilization> findByCrewMembersCardIdContains(String cardId);
}

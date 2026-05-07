package com.inlaco.crewmgrservice.feature.crewmobilization.application.port.out;

import com.inlaco.crewmgrservice.feature.crewmobilization.application.model.CrewMobilizationSearchCriteria;
import com.inlaco.crewmgrservice.feature.crewmobilization.domain.model.CrewMobilization;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CrewMobilizationRepository {

  CrewMobilization save(CrewMobilization schedule);

  Optional<CrewMobilization> findById(String id);

  Page<CrewMobilization> findAll(Pageable pageable);

  Page<CrewMobilization> findAll(CrewMobilizationSearchCriteria criteria, Pageable pageable);

  // List<AssignedMobilization> findByCrewMembersCardIdContains(String cardId);
}

package com.inlaco.crewmgrservice.feature.crewrental.application.port.out;

import com.inlaco.crewmgrservice.feature.crewrental.application.model.CrewRentalRequestSearchCriteria;
import com.inlaco.crewmgrservice.feature.crewrental.domain.model.CrewRentalRequest;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CrewRentalRequestRepository {
  Optional<CrewRentalRequest> findById(String id);

  Page<CrewRentalRequest> findAll(Pageable pageable);

  Page<CrewRentalRequest> findAll(CrewRentalRequestSearchCriteria criteria, Pageable pageable);

  CrewRentalRequest save(CrewRentalRequest request);
}

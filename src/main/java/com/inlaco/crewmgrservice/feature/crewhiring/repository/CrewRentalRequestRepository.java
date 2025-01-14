package com.inlaco.crewmgrservice.feature.crewhiring.repository;

import com.inlaco.crewmgrservice.feature.crewhiring.enums.CrewRentalRequestStatus;
import com.inlaco.crewmgrservice.feature.crewhiring.model.CrewRentalRequest;
import java.time.Instant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CrewRentalRequestRepository extends MongoRepository<CrewRentalRequest, String> {
  Page<CrewRentalRequest> findByStatus(CrewRentalRequestStatus status, Pageable pageable);

  Page<CrewRentalRequest> findByCompanyName(String companyName, Pageable pageable);

  Page<CrewRentalRequest> findByEstimatedDepartureTimeBetween(
      Instant start, Instant end, Pageable pageable);
}

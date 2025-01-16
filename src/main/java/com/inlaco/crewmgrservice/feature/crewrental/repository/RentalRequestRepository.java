package com.inlaco.crewmgrservice.feature.crewrental.repository;

import com.inlaco.crewmgrservice.feature.crewrental.enums.RentalRequestStatus;
import com.inlaco.crewmgrservice.feature.crewrental.model.RentalRequest;
import java.time.Instant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RentalRequestRepository extends MongoRepository<RentalRequest, String> {
  Page<RentalRequest> findByStatus(RentalRequestStatus status, Pageable pageable);

  Page<RentalRequest> findByCompanyName(String companyName, Pageable pageable);

  Page<RentalRequest> findByEstimatedDepartureTimeBetween(
      Instant start, Instant end, Pageable pageable);
}

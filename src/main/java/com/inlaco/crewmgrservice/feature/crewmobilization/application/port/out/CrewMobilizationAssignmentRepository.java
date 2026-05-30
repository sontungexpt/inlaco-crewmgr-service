package com.inlaco.crewmgrservice.feature.crewmobilization.application.port.out;

import com.inlaco.crewmgrservice.feature.crewmobilization.domain.model.CrewMobilizationAssignment;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CrewMobilizationAssignmentRepository {

  CrewMobilizationAssignment save(CrewMobilizationAssignment assignment);

  List<CrewMobilizationAssignment> saveAll(Iterable<CrewMobilizationAssignment> assignments);

  Optional<CrewMobilizationAssignment> findById(String id);

  List<CrewMobilizationAssignment> findByMobilizationId(String mobilizationId);

  List<CrewMobilizationAssignment> findByProfileId(String profileId);

  List<CrewMobilizationAssignment> findByProfileId(
      String profileId, Instant startDate, Instant endDate);

  boolean existsAssignmentOverlap(String profileId, Instant startDate, Instant endDate);

  List<CrewMobilizationAssignment> findNonEndedAssignmentsByEmployeeCardIds(
      Iterable<String> profileIds);

  List<CrewMobilizationAssignment> findAllActiveAssignments();

  List<CrewMobilizationAssignment> findAllActiveAssignmentsForClient(String clientId);

  Page<CrewMobilizationAssignment> findAllActiveAssignmentsForClient(
      String clientId, Pageable pageable);
}

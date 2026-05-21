package com.inlaco.crewmgrservice.feature.crewmobilization.application.port.out;

import com.inlaco.crewmgrservice.feature.crewmobilization.domain.model.CrewMobilizationAssignment;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

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
}

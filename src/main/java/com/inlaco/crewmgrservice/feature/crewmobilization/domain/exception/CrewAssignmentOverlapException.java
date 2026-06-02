package com.inlaco.crewmgrservice.feature.crewmobilization.domain.exception;

import com.inlaco.crewmgrservice.feature.crewmobilization.domain.error.CrewMobilizationErrorCode;
import com.inlaco.crewmgrservice.shared.kernel.exception.ApplicationException;
import java.time.Instant;
import java.util.List;

public class CrewAssignmentOverlapException extends ApplicationException {

  public record ConflictAssignment(
      String employeeCardId, String message, Instant startDate, Instant endDate) {}

  public CrewAssignmentOverlapException(List<ConflictAssignment> conflicts) {
    super(
        CrewMobilizationErrorCode.CREW_MOBILIZATION_ASSIGNMENT_OVERLAP_ERROR,
        "Some crews already busy now",
        conflicts);
  }

  public CrewAssignmentOverlapException(List<ConflictAssignment> conflicts, String message) {
    super(CrewMobilizationErrorCode.CREW_MOBILIZATION_ASSIGNMENT_OVERLAP_ERROR, message, conflicts);
  }
}

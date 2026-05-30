package com.inlaco.crewmgrservice.feature.shipschedule.application.service;

import com.inlaco.crewmgrservice.feature.shipschedule.domain.error.ShipScheduleErrorCode;
import com.inlaco.crewmgrservice.shared.kernel.exception.ApplicationException;
import java.time.Instant;
import java.util.List;

public class CrewAssignmentBusyException extends ApplicationException {

  public record ConflictAssignment(
      String employeeCardId, String message, Instant startDate, Instant endDate) {}

  public CrewAssignmentBusyException(List<ConflictAssignment> conflicts) {
    super(
        ShipScheduleErrorCode.CREW_ALREADY_ASSIGNED,
        String.format("Some crews already busy"),
        conflicts);
  }
}

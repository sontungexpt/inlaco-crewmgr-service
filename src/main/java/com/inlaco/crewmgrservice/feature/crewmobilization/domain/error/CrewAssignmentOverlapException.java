package com.inlaco.crewmgrservice.feature.crewmobilization.domain.error;

public class CrewAssignmentOverlapException extends RuntimeException {

  public CrewAssignmentOverlapException(String employeeCardId) {

    super("Crew already assigned in overlapping period: " + employeeCardId);
  }
}

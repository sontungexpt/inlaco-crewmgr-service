package com.inlaco.crewmgrservice.feature.crew.domain.enums;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

public enum CrewStatus {
  DRAFT,
  READY_FOR_ASSIGNMENT,
  ENGAGED,
  ON_LEAVE,
  INACTIVE;

  private EnumSet<CrewStatus> allowedTransitions;

  static {
    DRAFT.allowedTransitions = EnumSet.noneOf(CrewStatus.class);
    READY_FOR_ASSIGNMENT.allowedTransitions = EnumSet.of(ENGAGED, ON_LEAVE, INACTIVE);
    ENGAGED.allowedTransitions = EnumSet.of(READY_FOR_ASSIGNMENT, ON_LEAVE, INACTIVE);
    ON_LEAVE.allowedTransitions = EnumSet.of(READY_FOR_ASSIGNMENT, INACTIVE);
    INACTIVE.allowedTransitions = EnumSet.noneOf(CrewStatus.class);
  }

  public boolean canTransitionTo(CrewStatus target) {
    return allowedTransitions.contains(target);
  }

  public void validateTransition(CrewStatus target) {
    if (!canTransitionTo(target)) {
      throw new IllegalStateException("Cannot transition from " + this + " to " + target);
    }
  }

  public Set<CrewStatus> getAllowedTransitions() {
    return Collections.unmodifiableSet(allowedTransitions);
  }
}

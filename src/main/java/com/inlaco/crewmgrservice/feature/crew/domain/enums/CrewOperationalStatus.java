package com.inlaco.crewmgrservice.feature.crew.domain.enums;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

public enum CrewOperationalStatus {

  /**
   * Crew profile exists but is not operationally ready.
   *
   * <p>Examples:
   *
   * <ul>
   *   <li>Missing mandatory documents
   *   <li>Profile incomplete
   *   <li>Contract inactive
   *   <li>Pending verification
   * </ul>
   */
  DRAFT,

  /** Crew is operationally active and available for assignment. */
  AVAILABLE,

  /**
   * Crew is temporarily unavailable for operational assignment.
   *
   * <p>Examples:
   *
   * <ul>
   *   <li>Annual leave
   *   <li>Sick leave
   *   <li>Training
   *   <li>Mandatory rest period
   * </ul>
   */
  TEMPORARILY_UNAVAILABLE,

  /** Crew is suspended due to disciplinary, compliance, or investigation reasons. */
  SUSPENDED,

  /**
   * Crew is permanently inactive and no longer operational.
   *
   * <p>Examples:
   *
   * <ul>
   *   <li>Resigned
   *   <li>Retired
   *   <li>Terminated
   * </ul>
   */
  INACTIVE;

  /** Allowed lifecycle transitions. */
  private EnumSet<CrewOperationalStatus> allowedTransitions;

  static {
    DRAFT.allowedTransitions = EnumSet.of(AVAILABLE, TEMPORARILY_UNAVAILABLE, INACTIVE);

    AVAILABLE.allowedTransitions = EnumSet.of(TEMPORARILY_UNAVAILABLE, SUSPENDED, INACTIVE);

    TEMPORARILY_UNAVAILABLE.allowedTransitions = EnumSet.of(AVAILABLE, SUSPENDED, INACTIVE);

    SUSPENDED.allowedTransitions = EnumSet.of(AVAILABLE, INACTIVE);

    INACTIVE.allowedTransitions = EnumSet.noneOf(CrewOperationalStatus.class);
  }

  /** Returns whether transition to target status is allowed. */
  public boolean canTransitionTo(CrewOperationalStatus target) {
    return allowedTransitions.contains(target);
  }

  /** Validates transition and throws exception if invalid. */
  public void validateTransition(CrewOperationalStatus target) {
    if (!canTransitionTo(target)) {
      throw new IllegalStateException("Cannot transition from " + this + " to " + target);
    }
  }

  /** Returns immutable allowed transitions. */
  public Set<CrewOperationalStatus> getAllowedTransitions() {
    return Collections.unmodifiableSet(allowedTransitions);
  }
}

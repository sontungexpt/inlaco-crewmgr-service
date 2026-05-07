package com.inlaco.crewmgrservice.feature.crew.domain.enums;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

/**
 * Represents the lifecycle status of a crew member within the system.
 *
 * <p>Important: - This status is a HIGH-LEVEL / DERIVED state used for quick checks and UI display.
 * - The actual source of truth for assignments MUST come from CrewMobilization.
 *
 * <p>Typical lifecycle: DRAFT -> READY_FOR_ASSIGNMENT -> ASSIGNED -> ENGAGED ->
 * READY_FOR_ASSIGNMENT
 */
public enum CrewStatus {

  /** Initial state when crew profile is created but not ready for assignment. */
  DRAFT,

  /** Crew is available and ready to be assigned to a mobilization. */
  READY_FOR_ASSIGNMENT,

  /**
   * Crew has been assigned to a mobilization schedule, but has not started working yet (before
   * startDate).
   */
  ASSIGNED,

  /** Crew is actively working in a mobilization (within startDate - endDate). */
  ENGAGED,

  /** Crew is temporarily unavailable due to leave. */
  ON_LEAVE,

  /** Crew is permanently inactive (e.g., retired, terminated). */
  INACTIVE;

  /**
   * Defines allowed transitions between states. This acts as a simple state machine to prevent
   * invalid transitions.
   */
  private EnumSet<CrewStatus> allowedTransitions;

  static {
    // Draft can only become ready
    DRAFT.allowedTransitions = EnumSet.of(READY_FOR_ASSIGNMENT);

    // Ready crew can be assigned, go on leave, or become inactive
    READY_FOR_ASSIGNMENT.allowedTransitions = EnumSet.of(ASSIGNED, ON_LEAVE, INACTIVE);

    // Assigned crew can start working, be unassigned, or become inactive
    ASSIGNED.allowedTransitions = EnumSet.of(ENGAGED, READY_FOR_ASSIGNMENT, INACTIVE);

    // Working crew can finish assignment, go on leave, or become inactive
    ENGAGED.allowedTransitions = EnumSet.of(READY_FOR_ASSIGNMENT, ON_LEAVE, INACTIVE);

    // On leave crew can return or become inactive
    ON_LEAVE.allowedTransitions = EnumSet.of(READY_FOR_ASSIGNMENT, INACTIVE);

    // Inactive is terminal state
    INACTIVE.allowedTransitions = EnumSet.noneOf(CrewStatus.class);
  }

  /** Checks whether a transition to the target status is allowed. */
  public boolean canTransitionTo(CrewStatus target) {
    return allowedTransitions.contains(target);
  }

  /** Validates the transition and throws exception if invalid. */
  public void validateTransition(CrewStatus target) {
    if (!canTransitionTo(target)) {
      throw new IllegalStateException("Cannot transition from " + this + " to " + target);
    }
  }

  /** Returns an immutable view of allowed transitions. */
  public Set<CrewStatus> getAllowedTransitions() {
    return Collections.unmodifiableSet(allowedTransitions);
  }

  // =========================
  // Helper methods (domain semantics)
  // =========================

  /** Returns true if crew is part of a mobilization (either scheduled or active). */
  public boolean isAssigned() {
    return this == ASSIGNED || this == ENGAGED;
  }

  /** Returns true if crew is free and can be assigned. */
  public boolean isAvailable() {
    return this == READY_FOR_ASSIGNMENT;
  }

  /** Returns true if crew is actively working. */
  public boolean isWorking() {
    return this == ENGAGED;
  }
}

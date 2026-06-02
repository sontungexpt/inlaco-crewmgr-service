package com.inlaco.crewmgrservice.feature.shipschedule.domain.enums;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

public enum ScheduleStatus {
  DRAFT,
  CONFIRMED,
  IN_PROGRESS,
  COMPLETED,
  CANCELLED;

  private EnumSet<ScheduleStatus> allowedTransitions;

  static {
    DRAFT.allowedTransitions = EnumSet.of(CONFIRMED, CANCELLED);

    CONFIRMED.allowedTransitions = EnumSet.of(IN_PROGRESS, CANCELLED);

    IN_PROGRESS.allowedTransitions = EnumSet.of(COMPLETED, CANCELLED);

    COMPLETED.allowedTransitions = EnumSet.noneOf(ScheduleStatus.class);

    CANCELLED.allowedTransitions = EnumSet.noneOf(ScheduleStatus.class);
  }

  public boolean canTransitionTo(ScheduleStatus target) {
    return allowedTransitions.contains(target);
  }

  public Set<ScheduleStatus> allowedTransitions() {
    return Collections.unmodifiableSet(allowedTransitions);
  }

  public void validateTransition(ScheduleStatus target) {
    if (!canTransitionTo(target)) {
      throw new IllegalStateException("Cannot transition from " + this + " to " + target);
    }
  }
}

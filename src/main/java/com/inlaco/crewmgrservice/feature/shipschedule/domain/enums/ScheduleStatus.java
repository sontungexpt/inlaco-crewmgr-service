package com.inlaco.crewmgrservice.feature.shipschedule.domain.enums;

public enum ScheduleStatus {
  DRAFT,
  CONFIRMED,
  IN_PROGRESS,
  COMPLETED,
  CANCELLED;

  public void validateTransition(ScheduleStatus newStatus) {
    switch (this) {
      case DRAFT:
        if (newStatus != CONFIRMED && newStatus != CANCELLED) {
          throw new IllegalStateException(
              "Can only transition from DRAFT to CONFIRMED or CANCELLED");
        }
        break;
      case CONFIRMED:
        if (newStatus != IN_PROGRESS && newStatus != CANCELLED) {
          throw new IllegalStateException(
              "Can only transition from CONFIRMED to IN_PROGRESS or CANCELLED");
        }
        break;
      case IN_PROGRESS:
        if (newStatus != COMPLETED && newStatus != CANCELLED) {
          throw new IllegalStateException(
              "Can only transition from IN_PROGRESS to COMPLETED or CANCELLED");
        }
        break;
      case COMPLETED:
        throw new IllegalStateException("Cannot transition from COMPLETED status");
      case CANCELLED:
        throw new IllegalStateException("Cannot transition from CANCELLED status");
    }
  }
}

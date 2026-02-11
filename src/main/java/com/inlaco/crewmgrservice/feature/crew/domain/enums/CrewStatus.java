package com.inlaco.crewmgrservice.feature.crew.domain.enums;

public enum CrewStatus {
  READY_FOR_ASSIGNMENT,

  ENGAGED,

  ON_LEAVE,

  INACTIVE;

  public boolean canTransitionTo(CrewStatus target) {
    return switch (this) {
      case READY_FOR_ASSIGNMENT -> target == ENGAGED || target == ON_LEAVE;

      case ENGAGED -> target == READY_FOR_ASSIGNMENT || target == ON_LEAVE;

      case ON_LEAVE -> target == READY_FOR_ASSIGNMENT;

      case INACTIVE -> false;
    };
  }
}

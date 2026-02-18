package com.inlaco.crewmgrservice.feature.recruitment.domain.enums;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

public enum ApplicationStatus {
  APPLIED,
  SCREENING,
  INTERVIEW_SCHEDULED,
  INTERVIEWED,
  OFFERED,
  CONFIRMED,
  CONTRACT_PENDING_SIGNATURE,
  CONTRACT_SIGNED,
  HIRED, // onboarding when contract is signed and active
  REJECTED,
  WITHDRAWN;

  private EnumSet<ApplicationStatus> allowedTransitions;

  static {
    APPLIED.allowedTransitions = EnumSet.of(SCREENING, REJECTED, WITHDRAWN);
    SCREENING.allowedTransitions = EnumSet.of(INTERVIEW_SCHEDULED, REJECTED, WITHDRAWN);
    INTERVIEW_SCHEDULED.allowedTransitions = EnumSet.of(INTERVIEWED, REJECTED, WITHDRAWN);
    INTERVIEWED.allowedTransitions = EnumSet.of(OFFERED, REJECTED, WITHDRAWN);
    OFFERED.allowedTransitions = EnumSet.of(CONFIRMED, REJECTED, WITHDRAWN);
    CONFIRMED.allowedTransitions = EnumSet.of(CONTRACT_PENDING_SIGNATURE, REJECTED, WITHDRAWN);
    CONTRACT_PENDING_SIGNATURE.allowedTransitions = EnumSet.of(HIRED, REJECTED, WITHDRAWN);
    CONTRACT_SIGNED.allowedTransitions = EnumSet.of(HIRED, REJECTED, WITHDRAWN);
    HIRED.allowedTransitions = EnumSet.noneOf(ApplicationStatus.class);
    REJECTED.allowedTransitions = EnumSet.noneOf(ApplicationStatus.class);
    WITHDRAWN.allowedTransitions = EnumSet.noneOf(ApplicationStatus.class);
  }

  public boolean canTransitionTo(ApplicationStatus target) {
    return allowedTransitions.contains(target);
  }

  public Set<ApplicationStatus> getAllowedTransitions() {
    return Collections.unmodifiableSet(allowedTransitions);
  }

  public void validateTransition(ApplicationStatus target) throws IllegalStateException {
    if (!canTransitionTo(target)) {
      throw new IllegalStateException("Cannot transition from " + this + " to " + target);
    }
  }
}

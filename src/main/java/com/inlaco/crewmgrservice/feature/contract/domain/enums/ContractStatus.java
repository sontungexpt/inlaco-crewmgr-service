package com.inlaco.crewmgrservice.feature.contract.domain.enums;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

public enum ContractStatus {
  DRAFT,
  SIGNED,
  ACTIVE,
  EXPIRED,
  CANCELLED;

  private EnumSet<ContractStatus> allowedTransitions;

  static {
    DRAFT.allowedTransitions = EnumSet.of(SIGNED, CANCELLED);
    SIGNED.allowedTransitions = EnumSet.of(ACTIVE, CANCELLED);
    ACTIVE.allowedTransitions = EnumSet.of(EXPIRED, CANCELLED);
    EXPIRED.allowedTransitions = EnumSet.noneOf(ContractStatus.class);
    CANCELLED.allowedTransitions = EnumSet.noneOf(ContractStatus.class);
  }

  public boolean canTransitionTo(ContractStatus target) {
    return allowedTransitions.contains(target);
  }

  public Set<ContractStatus> allowedTransitions() {
    return Collections.unmodifiableSet(allowedTransitions);
  }

  public void validateTransition(ContractStatus target) throws IllegalStateException {
    if (!canTransitionTo(target)) {
      throw new IllegalStateException("Cannot transition from " + this + " to " + target);
    }
  }
}

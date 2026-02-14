package com.inlaco.crewmgrservice.feature.recruitment.domain.enums;

import java.util.Set;

public enum ApplicationStatus {
  APPLIED {
    @Override
    public Set<ApplicationStatus> getAllowedTransitions() {
      return Set.of(SCREENING, REJECTED, WITHDRAWN);
    }
  },

  SCREENING {
    @Override
    public Set<ApplicationStatus> getAllowedTransitions() {
      return Set.of(INTERVIEW_SCHEDULED, REJECTED, WITHDRAWN);
    }
  },

  INTERVIEW_SCHEDULED {
    @Override
    public Set<ApplicationStatus> getAllowedTransitions() {
      return Set.of(INTERVIEWED, REJECTED, WITHDRAWN);
    }
  },

  INTERVIEWED {
    @Override
    public Set<ApplicationStatus> getAllowedTransitions() {
      return Set.of(OFFERED, REJECTED, WITHDRAWN);
    }
  },

  OFFERED {
    @Override
    public Set<ApplicationStatus> getAllowedTransitions() {
      return Set.of(CONFIRMED, REJECTED, WITHDRAWN);
    }
  },

  CONFIRMED {
    @Override
    public Set<ApplicationStatus> getAllowedTransitions() {
      return Set.of(HIRED, REJECTED, WITHDRAWN);
    }
  },

  HIRED,
  REJECTED,
  WITHDRAWN;

  public Set<ApplicationStatus> getAllowedTransitions() {
    return Set.of();
  }

  public boolean canTransitionTo(ApplicationStatus target) {
    return getAllowedTransitions().contains(target);
  }
}

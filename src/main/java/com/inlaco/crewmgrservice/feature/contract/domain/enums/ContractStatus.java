package com.inlaco.crewmgrservice.feature.contract.domain.enums;

public enum ContractStatus {
  DRAFT {
    @Override
    public boolean canTransitionTo(ContractStatus target) {
      return target == SIGNED || target == CANCELLED;
    }
  },

  SIGNED {
    @Override
    public boolean canTransitionTo(ContractStatus target) {
      return target == ACTIVE || target == CANCELLED;
    }
  },

  ACTIVE {
    @Override
    public boolean canTransitionTo(ContractStatus target) {
      return target == EXPIRED || target == CANCELLED;
    }
  },

  EXPIRED {
    @Override
    public boolean canTransitionTo(ContractStatus target) {
      return false;
    }
  },

  CANCELLED {
    @Override
    public boolean canTransitionTo(ContractStatus target) {
      return false;
    }
  };

  public abstract boolean canTransitionTo(ContractStatus target);
}

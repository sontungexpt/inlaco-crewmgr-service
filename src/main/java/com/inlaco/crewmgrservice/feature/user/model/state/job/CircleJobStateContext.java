package com.inlaco.crewmgrservice.feature.user.model.state.job;

import com.inlaco.crewmgrservice.feature.user.model.User;

public class CircleJobStateContext {

  private UserJobState state;

  public CircleJobStateContext(User user) {
    switch (user.getJobState()) {
      case CANDIDATE:
        state = new CandidateState(user);
        break;
      case SAILOR:
        state = new SailorState(user);
        break;
      case CAN_APPLY:
        state = new CanApplyState(user);
        break;
      default:
        break;
    }
  }

  public CircleJobStateContext(UserJobState currentState) {
    this.state = currentState;
  }

  public UserJobState getCurrentState() {
    return state;
  }

  public void setState(UserJobState state) {
    this.state = state;
  }

  public void promote() {
    UserJobState oldState = state;
    state.handleBeforePromote(oldState);
    state.promote(this);
    state.handleAfterDemote(oldState);
  }

  public void demote() {
    UserJobState oldState = state;
    state.handleBeforePromote(oldState);
    state.demote(this);
    state.handleAfterDemote(oldState);
  }
}

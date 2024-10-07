package com.inlaco.crewmgrservice.feature.user.service.state;


// @Component
public class UserStateContext {

  private UserJobState state;

  public UserStateContext(UserJobState currentState) {
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

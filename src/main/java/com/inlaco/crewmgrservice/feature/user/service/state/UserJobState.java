package com.inlaco.crewmgrservice.feature.user.service.state;

import com.inlaco.crewmgrservice.feature.user.model.User;

public abstract class UserJobState {

  protected User user;

  public UserJobState(User user) {
    this.user = user;
  }

  public User.JobState getJobState() {
    return user.getJobState();
  }

  public abstract void promote(UserStateContext context);

  public abstract void demote(UserStateContext context);

  public void handleBeforePromote(UserJobState oldState) {}

  public void handleAfterPromote(UserJobState oldState) {}

  public void handleBeforeDemote(UserJobState oldState) {}

  public void handleAfterDemote(UserJobState oldState) {}
}

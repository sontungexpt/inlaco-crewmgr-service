package com.inlaco.crewmgrservice.feature.user.service.state;

import com.inlaco.crewmgrservice.feature.user.model.User;

public class BasicState extends UserJobState {

  public BasicState(User user) {
    super(user);
  }

  @Override
  public void promote(UserStateContext context) {
    context.setState(new CandidateState(user));
  }

  @Override
  public void demote(UserStateContext context) {
    context.setState(new BasicState(user));
  }
}

package com.inlaco.crewmgrservice.feature.user.service.state;

import com.inlaco.crewmgrservice.feature.user.model.User;

public class CandidateState extends UserJobState {

  // private CandidateRepository candidateRepository;
  // private UserRepository userRepository;

  // public CandidateState(
  //     User user, UserRepository userRepository, CandidateRepository candidateRepository) {
  //   super(user);
  //   this.candidateRepository = candidateRepository;
  // }

  public CandidateState(User user) {
    super(user);
  }

  @Override
  public void promote(UserStateContext context) {
    context.setState(new SailorState(user));
  }

  @Override
  public void demote(UserStateContext context) {
    context.setState(new BasicState(user));
  }
}

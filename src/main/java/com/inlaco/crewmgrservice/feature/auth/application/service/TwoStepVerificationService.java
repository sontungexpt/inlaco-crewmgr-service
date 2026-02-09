package com.inlaco.crewmgrservice.feature.auth.application.service;

import com.inlaco.crewmgrservice.feature.auth.application.enums.VerificationPolicy;
import com.inlaco.crewmgrservice.feature.auth.application.port.in.TwoStepVerificationUseCase;
import com.inlaco.crewmgrservice.feature.user.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TwoStepVerificationService implements TwoStepVerificationUseCase {

  private final TwoStepVerificationDispatcher twoStepVerificationDispatcher;

  @Override
  public void resend(User user) {
    twoStepVerificationDispatcher.resend(VerificationPolicy.EMAIL, user);
  }

  @Override
  public void verify(String token) {
    twoStepVerificationDispatcher.verify(VerificationPolicy.EMAIL, token);
  }
}

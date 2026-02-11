package com.inlaco.crewmgrservice.feature.auth.application.port.in;

import com.inlaco.crewmgrservice.feature.auth.application.enums.VerificationPolicy;
import com.inlaco.crewmgrservice.feature.user.domain.model.User;

public interface TwoStepVerificationService {

  VerificationPolicy getPolicy();

  void send(User user);

  void resend(User user);

  void verify(String token);
}

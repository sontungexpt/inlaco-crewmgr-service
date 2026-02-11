package com.inlaco.crewmgrservice.feature.auth.application.service;

import com.inlaco.crewmgrservice.feature.auth.application.enums.VerificationPolicy;
import com.inlaco.crewmgrservice.feature.auth.application.port.in.TwoStepVerificationService;
import com.inlaco.crewmgrservice.feature.user.domain.model.User;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class TwoStepVerificationDispatcher {

  private final Map<VerificationPolicy, TwoStepVerificationService> serviceMap;

  public TwoStepVerificationDispatcher(
      List<TwoStepVerificationService> twoStepVerificationServices) {
    serviceMap =
        twoStepVerificationServices.stream()
            .collect(
                Collectors.toMap(
                    TwoStepVerificationService::getPolicy,
                    twoStepVerificationService -> twoStepVerificationService));
  }

  public void send(VerificationPolicy policy, User user) {
    serviceMap.get(policy).send(user);
  }

  public void resend(VerificationPolicy policy, User user) {
    serviceMap.get(policy).resend(user);
  }

  public void verify(VerificationPolicy policy, String code) {
    serviceMap.get(policy).verify(code);
  }
}

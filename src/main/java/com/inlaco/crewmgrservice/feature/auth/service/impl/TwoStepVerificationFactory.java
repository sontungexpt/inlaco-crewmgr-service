package com.inlaco.crewmgrservice.feature.auth.service.impl;

import com.inlaco.crewmgrservice.feature.auth.service.TwoStepVerificationService;
import com.inlaco.crewmgrservice.feature.user.model.User;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TwoStepVerificationFactory {

  private final Map<String, TwoStepVerificationService> twoStepVerificationMap;

  public TwoStepVerificationService getTwoStepVerificationService(String type) {
    if (!twoStepVerificationMap.containsKey(type)) {
      throw new IllegalArgumentException("Invalid two step verification type");
    }
    return twoStepVerificationMap.get(type);
  }

  public void sendVerificationCode(String type, User user) {
    getTwoStepVerificationService(type).send(user);
  }

  public void verifyCode(String type, String code) {
    getTwoStepVerificationService(type).verify(code);
  }

  public void resendVerificationCode(String type, User user) {
    getTwoStepVerificationService(type).resend(user);
  }
}

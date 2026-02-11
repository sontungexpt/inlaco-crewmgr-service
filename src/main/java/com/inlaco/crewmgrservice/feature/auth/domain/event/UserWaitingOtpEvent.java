package com.inlaco.crewmgrservice.feature.auth.domain.event;

import com.inlaco.crewmgrservice.feature.user.domain.model.User;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class UserWaitingOtpEvent extends ApplicationEvent {
  private final User user;

  public UserWaitingOtpEvent(Object source, User user) {
    super(source);
    this.user = user;
  }
}

package com.inlaco.crewmgrservice.feature.notify;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class NotificationRequest<S, R> {

  private S sender;
  private R recipient;
  private String message;

  public NotificationRequest(S sender, R recipient, String message) {
    assert recipient != null;
    assert message != null;

    this.sender = sender;
    this.recipient = recipient;
    this.message = message;
  }
}

package com.inlaco.crewmgrservice.feature.notify;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public abstract class NotificationRequest<S, R> {

  private S sender;
  private List<R> recipients;
  private String message;

  public R getFirstRecipient() {
    return recipients.isEmpty() ? null : recipients.get(0);
  }

  protected NotificationRequest(S sender, R recipient, String message) {
    this(sender, List.of(recipient), message);
  }

  protected NotificationRequest(S sender, List<R> recipients, String message) {
    assert recipients != null;
    assert message != null;

    this.sender = sender;
    this.recipients = recipients;
    this.message = message;
  }
}

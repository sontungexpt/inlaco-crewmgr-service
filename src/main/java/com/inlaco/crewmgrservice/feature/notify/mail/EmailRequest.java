package com.inlaco.crewmgrservice.feature.notify.mail;

import com.inlaco.crewmgrservice.feature.notify.NotificationRequest;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailRequest extends NotificationRequest<String, String> {

  protected String subject;

  protected EmailType emailType = EmailType.SIMPLE;

  public EmailRequest(String recipient, String message, String subject) {
    this(null, recipient, message, subject, EmailType.SIMPLE);
  }

  public EmailRequest(String sender, String recipient, String message, String subject) {
    this(sender, recipient, message, subject, EmailType.SIMPLE);
  }

  public EmailRequest(
      String sender, String recipient, String message, String subject, EmailType emailType) {
    super(sender, recipient, message);
    this.subject = subject;
    this.emailType = emailType;
  }
}

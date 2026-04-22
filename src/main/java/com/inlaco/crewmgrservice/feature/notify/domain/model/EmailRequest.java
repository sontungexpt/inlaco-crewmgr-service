package com.inlaco.crewmgrservice.feature.notify.domain.model;

import com.inlaco.crewmgrservice.feature.notify.domain.enums.EmailType;
import java.util.Collections;
import java.util.List;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class EmailRequest implements NotificationRequest {

  private String sender;
  private List<String> recipients;
  private String message;

  private String subject;

  @Default private EmailType emailType = EmailType.TEXT;

  private String[] cc = null;

  private String[] bcc = null;

  public EmailRequest(String sender, String recipient, String message, String subject) {
    this.subject = subject;
    this.sender = sender;
    this.recipients = Collections.singletonList(recipient);
    this.message = message;
  }

  public static EmailRequestBuilder<?, ?> html(String recipient, String message, String subject) {
    return html(List.of(recipient), message, subject);
  }

  public static EmailRequestBuilder<?, ?> html(
      List<String> recipients, String message, String subject) {
    return builder(EmailType.MIME, recipients, message, subject);
  }

  public static EmailRequestBuilder<?, ?> builder(
      EmailType type, List<String> recipients, String message, String subject) {
    return new EmailRequestBuilderImpl()
        .emailType(type)
        .recipients(recipients)
        .message(message)
        .subject(subject);
  }
}

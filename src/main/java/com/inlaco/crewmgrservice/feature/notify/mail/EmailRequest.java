package com.inlaco.crewmgrservice.feature.notify.mail;

import com.inlaco.crewmgrservice.common.model.File;
import com.inlaco.crewmgrservice.feature.notify.NotificationRequest;
import java.util.List;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class EmailRequest extends NotificationRequest<String, String> {

  protected String subject;

  @Default @Setter protected EmailType emailType = EmailType.TEXT;

  protected List<File> attachments = null;

  protected String[] cc = null;

  protected String[] bcc = null;

  public EmailRequest(String sender, String recipient, String message, String subject) {
    super(sender, recipient, message);
    this.subject = subject;
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

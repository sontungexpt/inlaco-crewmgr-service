package com.inlaco.crewmgrservice.feature.notify.mail;

import com.inlaco.crewmgrservice.common.model.File;
import com.inlaco.crewmgrservice.feature.notify.NotificationRequest;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class EmailRequest extends NotificationRequest<String, String> {

  protected String subject;

  @Setter protected EmailType emailType = EmailType.SIMPLE;

  protected List<File> attachments;

  protected String[] cc;

  protected String[] bcc;

  public static EmailRequestBuilder<?, ?> builder(
      String recipient, String message, String subject) {
    return new EmailRequestBuilderImpl().recipient(recipient).message(message).subject(subject);
  }

  public EmailRequest(String recipient, String message, String subject) {
    super(null, recipient, message);
    this.subject = subject;
  }

  public abstract static class EmailRequestBuilder<
          C extends EmailRequest, B extends EmailRequestBuilder<C, B>>
      extends NotificationRequestBuilder<String, String, C, B> {

    public B recipient(String recipient) {
      return self().recipients(List.of(recipient));
    }

    public B htmlMessage() {
      return emailType(EmailType.MIME);
    }
  }
}

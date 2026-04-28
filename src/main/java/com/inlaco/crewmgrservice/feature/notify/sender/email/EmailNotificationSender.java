package com.inlaco.crewmgrservice.feature.notify.sender.email;

import com.inlaco.crewmgrservice.feature.notify.sender.NotificationSender;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.mail.autoconfigure.MailProperties;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class EmailNotificationSender implements NotificationSender<EmailRequest, Void> {

  private final MailProperties mailProperties;
  private final JavaMailSender mailSender;

  @Override
  public Class<EmailRequest> getRequestType() {
    return EmailRequest.class;
  }

  @Override
  public Void sendNotification(EmailRequest request) {
    String sender =
        request.getSender() != null ? request.getSender() : mailProperties.getUsername();

    try {
      if (request.getEmailType() == EmailType.MIME) {
        sendHtmlMessage(sender, request);
      } else {
        sendSimpleMessage(sender, request);
      }
      log.info(
          "Email sent | type={} | from={} | to={} | subject={}",
          request.getEmailType(),
          sender,
          request.getRecipients(),
          request.getSubject());
      return null;

    } catch (Exception e) {
      log.error(
          "Failed to send email | type={} | from={} | to={} | subject={}",
          request.getEmailType(),
          sender,
          request.getRecipients(),
          request.getSubject(),
          e);
      throw new RuntimeException("Email sending failed", e);
    }
  }

  private Object sendSimpleMessage(String sender, EmailRequest request) {
    SimpleMailMessage message = new SimpleMailMessage();
    message.setFrom(sender);
    message.setTo(request.getRecipients().toArray(new String[0]));
    message.setSubject(request.getSubject());
    message.setText(request.getMessage());

    if (request.getCc() != null) {
      message.setCc(request.getCc());
    }
    if (request.getBcc() != null) {
      message.setBcc(request.getBcc());
    }

    mailSender.send(message);
    return null;
  }

  private Object sendHtmlMessage(String sender, EmailRequest request) throws MessagingException {
    MimeMessage message = mailSender.createMimeMessage();
    var helper = new MimeMessageHelper(message, true, "UTF-8");

    helper.setFrom(sender);
    helper.setTo(request.getRecipients().toArray(new String[0]));
    helper.setSubject(request.getSubject());
    helper.setText(request.getMessage(), true); // true = HTML

    if (request.getCc() != null) {
      helper.setCc(request.getCc());
    }
    if (request.getBcc() != null) {
      helper.setBcc(request.getBcc());
    }

    mailSender.send(message);
    return null;
  }
}

// Uncomment to use Resend
// package com.inlaco.crewmgrservice.feature.notify.mail;

// import com.inlaco.crewmgrservice.feature.notify.NotificationService;
// import com.inlaco.crewmgrservice.feature.notify.NotificationType;
// import com.resend.Resend;
// import com.resend.core.exception.ResendException;
// import com.resend.services.emails.model.CreateEmailOptions;
// import lombok.RequiredArgsConstructor;
// import lombok.extern.slf4j.Slf4j;
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.stereotype.Service;

// @Slf4j
// @RequiredArgsConstructor
// @Service(NotificationType.EMAIL)
// public class EmailNotificationServiceImpl implements NotificationService<EmailRequest> {

//   @Value("${resend.domain}")
//   private String DEFAULT_SENDER;

//   private final Resend resend;

//   @Override
//   public void sendNotification(EmailRequest request) {
//     if (request == null) {
//       log.warn("Email request is null, skip sending");
//       return;
//     }

//     String sender = request.getSender() != null ? request.getSender() : DEFAULT_SENDER;

//     if (sender == null || sender.isBlank()) {
//       log.error("No sender configured for email request");
//       return;
//     }

//     log.debug(
//         "Preparing email: type={}, subject={}, recipients={}",
//         request.getEmailType(),
//         request.getSubject(),
//         request.getRecipients());

//     CreateEmailOptions.Builder builder =
//         CreateEmailOptions.builder()
//             .from(sender)
//             .to(request.getRecipients())
//             .subject(request.getSubject());

//     if (request.getCc() != null) {
//       builder.cc(request.getCc());
//     }
//     if (request.getBcc() != null) {
//       builder.bcc(request.getBcc());
//     }

//     switch (request.getEmailType()) {
//       case MIME -> {
//         log.debug("Building HTML email");
//         builder.html(request.getMessage());
//       }
//       case TEXT -> {
//         log.debug("Building TEXT email");
//         builder.text(request.getMessage());
//       }
//       default -> {
//         log.warn("Unknown email type {}, fallback to TEXT", request.getEmailType());
//         builder.text(request.getMessage());
//       }
//     }

//     try {
//       resend.emails().send(builder.build());

//       log.info(
//           "Email [{}] sent successfully to {}", request.getEmailType(), request.getRecipients());

//     } catch (ResendException e) {
//       log.error(
//           "Failed to send email [{}] to {}", request.getEmailType(), request.getRecipients(), e);
//     }
//   }
// }

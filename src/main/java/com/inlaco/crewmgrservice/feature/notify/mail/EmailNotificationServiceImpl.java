package com.inlaco.crewmgrservice.feature.notify.mail;

import com.inlaco.crewmgrservice.feature.notify.NotificationService;
import com.inlaco.crewmgrservice.feature.notify.NotificationType;
import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service(NotificationType.EMAIL)
public class EmailNotificationServiceImpl implements NotificationService<EmailRequest> {

  @Value("${resend.domain}")
  private String DEFAULT_SENDER;

  private final Resend resend;

  @Override
  public void sendNotification(EmailRequest request) {
    if (request == null) {
      log.warn("Email request is null, skip sending");
      return;
    }

    String sender = request.getSender() != null ? request.getSender() : DEFAULT_SENDER;

    if (sender == null || sender.isBlank()) {
      log.error("No sender configured for email request");
      return;
    }

    log.debug(
        "Preparing email: type={}, subject={}, recipients={}",
        request.getEmailType(),
        request.getSubject(),
        request.getRecipients());

    CreateEmailOptions.Builder builder =
        CreateEmailOptions.builder()
            .from(sender)
            .to(request.getRecipients())
            .subject(request.getSubject());

    if (request.getCc() != null) {
      builder.cc(request.getCc());
    }
    if (request.getBcc() != null) {
      builder.bcc(request.getBcc());
    }

    switch (request.getEmailType()) {
      case MIME -> {
        log.debug("Building HTML email");
        builder.html(request.getMessage());
      }
      case TEXT -> {
        log.debug("Building TEXT email");
        builder.text(request.getMessage());
      }
      default -> {
        log.warn("Unknown email type {}, fallback to TEXT", request.getEmailType());
        builder.text(request.getMessage());
      }
    }

    try {
      resend.emails().send(builder.build());

      log.info(
          "Email [{}] sent successfully to {}", request.getEmailType(), request.getRecipients());

    } catch (ResendException e) {
      log.error(
          "Failed to send email [{}] to {}", request.getEmailType(), request.getRecipients(), e);
    }
  }
}

// Uncomment to use JavaMailSender via SMTP

// package com.inlaco.crewmgrservice.feature.notify.mail;

// import com.inlaco.crewmgrservice.feature.notify.NotificationService;
// import com.inlaco.crewmgrservice.feature.notify.NotificationType;
// import jakarta.mail.Message.RecipientType;
// import jakarta.mail.MessagingException;
// import jakarta.mail.internet.MimeMessage;
// import java.util.List;
// import lombok.RequiredArgsConstructor;
// import lombok.extern.slf4j.Slf4j;
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.mail.SimpleMailMessage;
// import org.springframework.mail.javamail.JavaMailSender;
// import org.springframework.stereotype.Service;

// @Slf4j
// @RequiredArgsConstructor
// @Service(NotificationType.EMAIL)
// public class EmailNotificationServiceImpl implements NotificationService<EmailRequest> {

//   @Value("${spring.mail.username}")
//   private String SENDER;

//   private final JavaMailSender mailSender;

//   private void sendSimpleMessage(String sender, EmailRequest request) {
//     SimpleMailMessage message = new SimpleMailMessage();
//     message.setFrom(sender);
//     message.setTo((String[]) request.getRecipients().toArray());
//     message.setSubject(request.getSubject());
//     message.setText(request.getMessage());
//     message.setCc(request.getCc());
//     message.setBcc(request.getBcc());

//     log.info("Sending simple email to {}", request.getRecipients());
//     mailSender.send(message);
//   }

//   private void sendHtmlMessage(String sender, EmailRequest request) {
//     MimeMessage message = mailSender.createMimeMessage();

//     try {
//       message.setFrom(sender);
//       for (String recipient : request.getRecipients()) {
//         message.addRecipients(RecipientType.TO, recipient);
//       }
//       message.setSubject(request.getSubject());
//       message.setText(request.getMessage(), "UTF-8", "html");
//       // message.setContent(request.getMessage(), "text/html");
//     } catch (MessagingException e) {
//       e.printStackTrace();
//     }

//     log.info("Sending http email to {}", request.getRecipients());
//     mailSender.send(message);
//   }

//   @Override
//   public void sendNotification(EmailRequest request) {
//     List<String> recipient = request.getRecipients();
//     String sender = request.getSender() != null ? request.getSender() : SENDER;

//     switch (request.getEmailType()) {
//       case MIME:
//         sendHtmlMessage(sender, request);
//         log.info("HTML email sent to {}", recipient);
//         break;
//       default:
//         sendSimpleMessage(sender, request);
//         log.info("Simple email sent to {}", recipient);
//     }
//   }
// }

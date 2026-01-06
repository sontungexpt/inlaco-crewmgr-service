package com.inlaco.crewmgrservice.feature.notify.mail;

import com.inlaco.crewmgrservice.feature.notify.NotificationService;
import com.inlaco.crewmgrservice.feature.notify.NotificationType;
import jakarta.mail.Message.RecipientType;
import jakarta.mail.internet.MimeMessage;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service(NotificationType.EMAIL)
public class EmailNotificationServiceImpl implements NotificationService<EmailRequest> {

  @Value("${spring.mail.username}")
  private String SENDER;

  private final JavaMailSender mailSender;

  private void sendSimpleMessage(String sender, EmailRequest request) {
    SimpleMailMessage message = new SimpleMailMessage();
    message.setFrom(sender);
    message.setTo((String[]) request.getRecipients().toArray());
    message.setSubject(request.getSubject());
    message.setText(request.getMessage());
    message.setCc(request.getCc());
    message.setBcc(request.getBcc());

    log.info("Sending simple email to {}", request.getRecipients());
    mailSender.send(message);
  }

  @SneakyThrows
  private void sendHtmlMessage(String sender, EmailRequest request) {
    MimeMessage message = mailSender.createMimeMessage();
    message.setFrom(sender);
    for (String recipient : request.getRecipients()) {
      message.addRecipients(RecipientType.TO, recipient);
    }
    message.setSubject(request.getSubject());
    message.setText(request.getMessage(), "UTF-8", "html");

    // message.setContent(request.getMessage(), "text/html");

    log.info("Sending http email to {}", request.getRecipients());
    mailSender.send(message);
  }

  @Override
  public void sendNotification(EmailRequest request) {
    List<String> recipient = request.getRecipients();
    String sender = request.getSender() != null ? request.getSender() : SENDER;

    switch (request.getEmailType()) {
      case MIME:
        sendHtmlMessage(sender, request);
        log.info("HTML email sent to {}", recipient);
        break;
      default:
        sendSimpleMessage(sender, request);
        log.info("Simple email sent to {}", recipient);
    }
  }
}

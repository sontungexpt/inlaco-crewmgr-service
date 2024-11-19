package com.inlaco.crewmgrservice.feature.notify.mail;

import com.inlaco.crewmgrservice.feature.notify.NotificationService;
import com.inlaco.crewmgrservice.feature.notify.NotificationType;
import jakarta.mail.Message;
import jakarta.mail.internet.MimeMessage;
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

  private void sendSimpleMessage(String sender, String to, String subject, String msg) {
    SimpleMailMessage message = new SimpleMailMessage();
    message.setFrom(sender);
    message.setTo(to);
    message.setSubject(subject);
    message.setText(msg);
    mailSender.send(message);
  }

  @SneakyThrows
  private void sendHtmlMessage(String sender, String to, String subject, String html) {
    MimeMessage message = mailSender.createMimeMessage();
    message.setFrom(sender);
    message.setRecipients(Message.RecipientType.TO, to);
    message.setSubject(subject);
    message.setContent(html, "text/html");
    mailSender.send(message);
  }

  @Override
  public void sendNotification(EmailRequest request) {
    String recipient = request.getRecipient();
    String subject = request.getSubject();
    String message = request.getMessage();
    String sender = request.getSender() != null ? request.getSender() : SENDER;

    switch (request.getEmailType()) {
      case HTML:
        sendHtmlMessage(sender, recipient, subject, message);
        break;
      default:
        sendSimpleMessage(sender, recipient, subject, message);
    }
  }
}

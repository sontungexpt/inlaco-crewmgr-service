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

  @Value("${spring.mail.username}")
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

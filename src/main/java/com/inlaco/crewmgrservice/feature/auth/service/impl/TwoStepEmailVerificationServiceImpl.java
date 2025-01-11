package com.inlaco.crewmgrservice.feature.auth.service.impl;

import com.inlaco.crewmgrservice.feature.auth.dto.ResendTokenResponse;
import com.inlaco.crewmgrservice.feature.auth.enums.TwoStepVerificationType;
import com.inlaco.crewmgrservice.feature.auth.exceptions.TwoStepVerificationException;
import com.inlaco.crewmgrservice.feature.auth.model.EmailVerificationToken;
import com.inlaco.crewmgrservice.feature.auth.repository.EmailVerificationTokenRepository;
import com.inlaco.crewmgrservice.feature.auth.service.TwoStepVerificationService;
import com.inlaco.crewmgrservice.feature.notify.NotificationFactory;
import com.inlaco.crewmgrservice.feature.notify.NotificationType;
import com.inlaco.crewmgrservice.feature.notify.mail.EmailRequest;
import com.inlaco.crewmgrservice.feature.notify.mail.EmailType;
import com.inlaco.crewmgrservice.feature.user.model.User;
import com.inlaco.crewmgrservice.feature.user.service.UserService;
import com.inlaco.crewmgrservice.utils.HttpServletUtils;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yaml.snakeyaml.util.UriEncoder;

@Service(TwoStepVerificationType.EMAIL)
@RequiredArgsConstructor
@Slf4j
public class TwoStepEmailVerificationServiceImpl implements TwoStepVerificationService {
  private String SUBJECT = "Inlaco - Verify your email";

  private final EmailVerificationTokenRepository emailVerificationTokenRepository;
  private final UserService userService;
  private final NotificationFactory notificationFactory;

  @Value("${inlaco.api.url.v1}")
  private String API_V1_URL;

  @Value("${inlaco.client.base.endpoint.login}")
  private String LOGIN_CLIENT_URL;

  private EmailRequest generateEmailRequest(User user, EmailVerificationToken token) {
    try {
      String link = generateVerificationLink(token);
      String html =
          Files.readString(
              Paths.get("src/main/resources/templates/email/html/two-step-verification.html"));

      return EmailRequest.builder(
              user.getUsername(),
              html.replace("${name}", user.getName()).replace("${verificationLink}", link),
              SUBJECT)
          .emailType(EmailType.MIME)
          .build();
    } catch (IOException e) {
      e.printStackTrace();
    }
    throw new RuntimeException("Failed to generate email request");
  }

  @Override
  @Transactional
  public ResendTokenResponse send(User user) {
    var savedToken =
        emailVerificationTokenRepository.save(new EmailVerificationToken(user.getId()));

    var emailRequest = generateEmailRequest(user, savedToken);

    notificationFactory.sendNotificationAsync(NotificationType.EMAIL, emailRequest);
    log.info("Email verification token sent to user {}", user.getUsername());
    return generateResendTokenResponse(savedToken);
  }

  private EmailVerificationToken refreshToken(EmailVerificationToken unrefreshToken) {
    emailVerificationTokenRepository.deleteById(unrefreshToken.getId());
    return emailVerificationTokenRepository.save(
        (EmailVerificationToken) unrefreshToken.refresh(true));
  }

  private ResendTokenResponse generateResendTokenResponse(EmailVerificationToken token) {
    return new ResendTokenResponse(token.getToken(), token.getIssuedDate(), token.getIssuedDate());
  }

  @Override
  public ResendTokenResponse resend(User user) {
    var token = emailVerificationTokenRepository.findByUserId(user.getId()).orElse(null);
    if (token != null) {
      var refreshedToken = refreshToken(token);
      var emailRequest = generateEmailRequest(user, refreshedToken);

      notificationFactory.sendNotificationAsync(NotificationType.EMAIL, emailRequest);
      log.info("Email verification token resent to user {}", user.getUsername());
      return generateResendTokenResponse(refreshedToken);
    }
    return send(user);
  }

  @Override
  public void verify(String unCheckedToken) {
    var token =
        emailVerificationTokenRepository
            .findByToken(unCheckedToken)
            .orElseThrow(() -> new TwoStepVerificationException("Token expired"));

    log.info("Email verification token verified for user {}", token.getUserId());
    var user = userService.findUserById(token.getUserId());
    user.activate();
    userService.saveUser(user);
    emailVerificationTokenRepository.deleteById(token.getId());

    HttpServletUtils.getResponse()
        .ifPresent(
            (response) -> {
              try {
                response.sendRedirect(LOGIN_CLIENT_URL);
              } catch (IOException e) {
                e.printStackTrace();
              }
            });
  }

  private String generateVerificationLink(EmailVerificationToken emailVerificationToken) {
    String link =
        API_V1_URL
            + "/auth/two-step-verification?token="
            + UriEncoder.encode(emailVerificationToken.getToken());
    log.info("Generated verification link: {}", link);
    return link;
  }
}

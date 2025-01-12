package com.inlaco.crewmgrservice.feature.auth.service.impl;

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
import com.inlaco.crewmgrservice.utils.Base64EncryptionUtils;
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
  private String EMAIL_TEMPLATE_PATH =
      "src/main/resources/templates/email/html/two-step-verification.html";

  private final EmailVerificationTokenRepository emailVerificationTokenRepository;
  private final UserService userService;
  private final NotificationFactory notificationFactory;

  @Value("${inlaco.client.endpoint.login}")
  private String LOGIN_CLIENT_URL;

  private EmailRequest generateEmailRequest(User user, EmailVerificationToken token) {
    try {
      String link = generateVerificationLink(token);
      String html = Files.readString(Paths.get(EMAIL_TEMPLATE_PATH));

      return EmailRequest.builder(
              user.getUsername(),
              html.replace("${name}", user.getName()).replace("${verificationLink}", link),
              SUBJECT)
          .emailType(EmailType.MIME)
          .build();
    } catch (IOException e) {
      log.error("Failed to generate email request");
      e.printStackTrace();
    }
    throw new RuntimeException("Failed to generate email request");
  }

  @Override
  @Transactional
  public void send(User user) {
    var savedToken =
        emailVerificationTokenRepository.save(new EmailVerificationToken(user.getId()));

    var emailRequest = generateEmailRequest(user, savedToken);

    notificationFactory.sendNotificationAsync(NotificationType.EMAIL, emailRequest);
    log.info("Email verification token sent to user {}", user.getUsername());
  }

  private EmailVerificationToken refreshToken(EmailVerificationToken unrefreshToken) {
    emailVerificationTokenRepository.deleteById(unrefreshToken.getId());
    return emailVerificationTokenRepository.save(
        (EmailVerificationToken) unrefreshToken.refresh(true));
  }

  @Override
  public void resend(User user) {
    var token = emailVerificationTokenRepository.findByUserId(user.getId()).orElse(null);
    log.debug("Attempt to resend token for user {}", user.getUsername());
    if (token != null) {
      var refreshedToken = refreshToken(token);
      log.debug("Refreshed token: {}", refreshedToken.getToken());
      var emailRequest = generateEmailRequest(user, refreshedToken);

      notificationFactory.sendNotificationAsync(NotificationType.EMAIL, emailRequest);
      log.info("Email verification token resent to user {}", user.getUsername());
    }
    send(user);
  }

  @Override
  public void verify(String unCheckedToken) {

    log.info("Verifying email token");
    var decodedToken = Base64EncryptionUtils.decodeBetter(unCheckedToken);
    log.debug("Decoded token: {}", decodedToken);

    var token =
        emailVerificationTokenRepository
            .findByToken(decodedToken)
            .orElseThrow(() -> new TwoStepVerificationException("Token expired"));

    var user = userService.findUserById(token.getUserId());
    log.info("Email verification token verified for user {}", user.getName());

    user.activate();
    userService.saveUser(user);
    log.info("User {} activated", user.getName());

    emailVerificationTokenRepository.deleteById(token.getId());
    log.debug("Deleted token ID: {}", token.getId());

    HttpServletUtils.getResponse()
        .ifPresent(
            (response) -> {
              try {
                log.info("Redirecting user to login URL: {}", LOGIN_CLIENT_URL);
                response.sendRedirect(LOGIN_CLIENT_URL);
              } catch (IOException e) {
                log.error("Failed to redirect user to login URL: {}", LOGIN_CLIENT_URL);
                e.printStackTrace();
              }
            });
  }

  private String getServerBaseUrl() {
    return HttpServletUtils.getRequest()
        .map(
            (request) -> {
              String scheme = request.getScheme(); // http or https
              String host = request.getServerName(); // host name
              int port = request.getServerPort(); // Port
              return scheme + "://" + host + ":" + port;
            })
        .orElse("");
  }

  private String generateVerificationLink(EmailVerificationToken emailVerificationToken) {
    String link =
        getServerBaseUrl()
            + "/api/v1/auth/two-step-verification?token="
            + UriEncoder.encode(
                Base64EncryptionUtils.encodeBetter(emailVerificationToken.getToken(), 15));
    log.debug("Generated verification link: {}", link);
    return link;
  }
}

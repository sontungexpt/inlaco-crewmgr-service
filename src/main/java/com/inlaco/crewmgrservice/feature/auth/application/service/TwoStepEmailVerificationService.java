package com.inlaco.crewmgrservice.feature.auth.application.service;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.inlaco.crewmgrservice.feature.auth.application.enums.VerificationPolicy;
import com.inlaco.crewmgrservice.feature.auth.application.port.in.TwoStepVerificationService;
import com.inlaco.crewmgrservice.feature.auth.application.port.out.EmailVerificationTokenRepository;
import com.inlaco.crewmgrservice.feature.auth.domain.exception.TwoStepVerificationException;
import com.inlaco.crewmgrservice.feature.auth.domain.model.EmailVerificationToken;
import com.inlaco.crewmgrservice.feature.notify.NotificationDispatcher;
import com.inlaco.crewmgrservice.feature.notify.NotificationPolicy;
import com.inlaco.crewmgrservice.feature.notify.mail.EmailRequest;
import com.inlaco.crewmgrservice.feature.user.application.port.in.UserService;
import com.inlaco.crewmgrservice.feature.user.domain.model.User;
import com.inlaco.crewmgrservice.shared.crypto.DigestUtils;
import com.inlaco.crewmgrservice.shared.template.TextTemplateBuilder;
import com.inlaco.crewmgrservice.utils.HttpServletUtils;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.util.UriEncoder;

@RequiredArgsConstructor
@Slf4j
@Service
public class TwoStepEmailVerificationService implements TwoStepVerificationService {

  private final EmailVerificationTokenRepository emailVerificationTokenRepository;
  private final UserService userService;
  private final NotificationDispatcher notificationFactory;

  @Value("${inlaco.server.base-url}")
  private String SERVER_BASE_URL;

  @Value("${inlaco.client.endpoint.login}")
  private String LOGIN_CLIENT_URL;

  @Value("${inlaco.template.email.two-step-verification.subject:Inlaco - Verify your email}")
  private String SUBJECT;

  @Value("${inlaco.template.email.two-step-verification.path}")
  private String EMAIL_TEMPLATE_PATH;

  private volatile String cachedTemplate;

  @Override
  public VerificationPolicy getPolicy() {
    return VerificationPolicy.EMAIL;
  }

  @Override
  public void send(User user) {
    emailVerificationTokenRepository.deleteByUserId(user.getId());
    TokenPair pair = generateTokenPair();
    EmailVerificationToken token = new EmailVerificationToken(user.getId(), pair.hash());
    emailVerificationTokenRepository.save(token);
    sendEmail(user, pair.raw());
    log.debug("Email verification token sent to user {}", user.getUsername());
  }

  @Override
  public void resend(User user) {
    EmailVerificationToken token =
        emailVerificationTokenRepository
            .findByUserId(user.getId())
            .orElseThrow(() -> new TwoStepVerificationException("No active token"));

    if (!token.canResend()) {
      throw new TwoStepVerificationException("Please wait before resending verification email");
    }

    TokenPair pair = generateTokenPair();

    token.refresh(pair.hash()); // reset hash + TTL + resend time
    emailVerificationTokenRepository.save(token);
    sendEmail(user, pair.raw());
    log.debug("Email verification token resent to user {}", user.getUsername());
  }

  @Override
  public void verify(String rawToken) {
    String hash = hash(rawToken);
    EmailVerificationToken token =
        emailVerificationTokenRepository
            .findByHashToken(hash)
            .orElseThrow(() -> new TwoStepVerificationException("Token expired or invalid"));

    User user = userService.findUserById(token.getUserId());
    user.activate();
    userService.saveUser(user);

    emailVerificationTokenRepository.deleteById(token.getId());

    log.debug("Email verified for user {}", user.getUsername());

    redirectToLogin(user);
  }

  public void revoke(User user) {
    emailVerificationTokenRepository.deleteByUserId(user.getId());
    log.debug("Verification token revoked for user {}", user.getUsername());
  }

  public boolean hasActiveToken(User user) {
    return emailVerificationTokenRepository.findByUserId(user.getId()).isPresent();
  }

  private TokenPair generateTokenPair() {
    String raw = NanoIdUtils.randomNanoId();
    String hash = hash(raw);
    return new TokenPair(raw, hash);
  }

  private String hash(String raw) {
    return DigestUtils.sha256(raw);
  }

  private void sendEmail(User user, String rawToken) {
    notificationFactory.sendNotificationAsync(
        NotificationPolicy.EMAIL, generateEmailRequest(user, rawToken));
  }

  private void redirectToLogin(User user) {
    HttpServletUtils.getResponse()
        .ifPresent(
            response -> {
              try {
                response.sendRedirect(LOGIN_CLIENT_URL);
              } catch (IOException e) {
                log.error("Redirect failed for user {}", user.getUsername(), e);
              }
            });
  }

  private EmailRequest generateEmailRequest(User user, String token) {
    String link = generateVerificationLink(token);
    return EmailRequest.html(
            user.getUsername(),
            TextTemplateBuilder.content(getEmailTemplate())
                .var("name", user.getName())
                .var("verificationLink", link)
                .buildContent(),
            SUBJECT)
        .build();
  }

  private String generateVerificationLink(String token) {
    return SERVER_BASE_URL + "/api/v1/auth/two-step-verification?token=" + UriEncoder.encode(token);
  }

  /** Lazy-load the HTML template */
  private String getEmailTemplate() {
    if (cachedTemplate == null) {
      synchronized (this) {
        if (cachedTemplate == null) {
          try {
            ClassPathResource resource = new ClassPathResource(EMAIL_TEMPLATE_PATH);
            cachedTemplate =
                new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
          } catch (IOException e) {
            log.error("Failed to load email template with path {}", EMAIL_TEMPLATE_PATH, e);
            throw new RuntimeException("Failed to generate email request");
          }
        }
      }
    }
    return cachedTemplate;
  }

  // ======================================================
  // TOKEN PAIR
  // ======================================================
  private record TokenPair(String raw, String hash) {}
}

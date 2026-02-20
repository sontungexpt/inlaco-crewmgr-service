package com.inlaco.crewmgrservice.feature.auth.application.service;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.inlaco.crewmgrservice.feature.auth.application.enums.VerificationPolicy;
import com.inlaco.crewmgrservice.feature.auth.application.model.event.TwoStepVerificationSucceedEvent;
import com.inlaco.crewmgrservice.feature.auth.application.port.in.TwoStepVerificationService;
import com.inlaco.crewmgrservice.feature.auth.application.port.out.EmailVerificationTokenRepository;
import com.inlaco.crewmgrservice.feature.auth.domain.exception.TwoStepVerificationException;
import com.inlaco.crewmgrservice.feature.auth.domain.model.EmailVerificationToken;
import com.inlaco.crewmgrservice.feature.notify.NotificationDispatcher;
import com.inlaco.crewmgrservice.feature.notify.NotificationPolicy;
import com.inlaco.crewmgrservice.feature.notify.mail.EmailRequest;
import com.inlaco.crewmgrservice.feature.user.application.port.in.UserUseCase;
import com.inlaco.crewmgrservice.feature.user.domain.model.User;
import com.inlaco.crewmgrservice.shared.crypto.DigestUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.yaml.snakeyaml.util.UriEncoder;

@RequiredArgsConstructor
@Slf4j
@Service
@Transactional
public class EmailTwoStepVerificationService implements TwoStepVerificationService {

  private final EmailVerificationTokenRepository emailVerificationTokenRepository;
  private final UserUseCase userUseCase;
  private final NotificationDispatcher notificationFactory;
  private final ApplicationEventPublisher eventPublisher;
  private final SpringTemplateEngine templateEngine;

  @Value("${inlaco.server.base-url}")
  private String SERVER_BASE_URL;

  @Value("${inlaco.template.email.two-step-verification.subject:Inlaco - Verify your email}")
  private String SUBJECT;

  @Value("${inlaco.template.email.two-step-verification.path}")
  private String TEMPLATE_PATH;

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

    userUseCase.activate(token.getUserId(), "Email verified successful");
    emailVerificationTokenRepository.deleteById(token.getId());

    eventPublisher.publishEvent(new TwoStepVerificationSucceedEvent(token));

    log.debug("Email verified for user {}", token.getUserId());
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

  private EmailRequest generateEmailRequest(User user, String token) {
    String link = generateVerificationLink(token);
    String htmlContent = buildTwoStepVerification(user.getName(), link);
    return EmailRequest.html(user.getUsername(), htmlContent, SUBJECT).build();
  }

  private String generateVerificationLink(String token) {
    return UriEncoder.encode(SERVER_BASE_URL + "/api/v1/auth/two-step-verification?token=" + token);
  }

  public String buildTwoStepVerification(String name, String link) {
    Context context = new Context();
    context.setVariable("name", name);
    context.setVariable("verificationLink", link);
    return templateEngine.process(TEMPLATE_PATH, context);
  }

  static record TokenPair(String raw, String hash) {}
}

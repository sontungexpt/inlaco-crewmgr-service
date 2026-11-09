package com.inlaco.crewmgrservice.infrastructure.security.ratelimmter;

import com.inlaco.crewmgrservice.feature.notify.sender.email.EmailNotificationSender;
import com.inlaco.crewmgrservice.feature.notify.sender.email.EmailRequest;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.time.Duration;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.mail.autoconfigure.MailProperties;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Service;

/**
 * Distributed Token-Bucket rate limiter backed by Redis.
 *
 * <p>Protects all endpoints against abusive bots and brute-force attempts.
 * When continuous rejections occur, sends an alert email to the admin (${EMAIL_USERNAME})
 * with a cryptographically signed, single-use one-click link to block the IP, Account, or both.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RateLimiterService {

  private static final String BLOCK_PREFIX = "block:";
  private static final String BUCKET_PREFIX = "bucket:";
  private static final String ATTACK_PREFIX = "attack:";
  private static final String COOLDOWN_PREFIX = "attackCooldown:";
  private static final String TOKEN_PREFIX = "alertToken:";
  private static final String FAILED_ATTEMPTS_PREFIX = "alertFailedAttempts:";

  private static final SecureRandom SECURE_RANDOM = new SecureRandom();

  private static final RedisScript<List> TOKEN_BUCKET_SCRIPT =
      RedisScript.of(
          """
          local key = KEYS[1]
          local max_tokens = tonumber(ARGV[1])
          local refill_rate = tonumber(ARGV[2])
          local now = tonumber(ARGV[3])
          local requested = tonumber(ARGV[4])
          local ttl = tonumber(ARGV[5])

          local data = redis.call('HMGET', key, 'tokens', 'last_refill')
          local tokens = tonumber(data[1])
          local last_refill = tonumber(data[2])

          if tokens == nil then
            tokens = max_tokens
            last_refill = now
          else
            local elapsed = math.max(0, now - last_refill)
            tokens = math.min(max_tokens, tokens + elapsed * refill_rate)
            last_refill = now
          end

          local allowed = 0
          if tokens >= requested then
            tokens = tokens - requested
            allowed = 1
          end

          redis.call('HSET', key, 'tokens', tostring(tokens), 'last_refill', tostring(last_refill))
          redis.call('EXPIRE', key, ttl)

          return {allowed, math.floor(tokens)}
          """,
          List.class);

  private final StringRedisTemplate redisTemplate;
  private final EmailNotificationSender emailSender;
  private final MailProperties mailProperties;

  @Value("${inlaco.server.base-url:http://localhost:8080}")
  private String serverBaseUrl;

  @Value("${token.access-token.secret-key:${JWT_ACCESS_SECRET_KEY:inlaco-default-security-signing-secret-key-32bytes}}")
  private String signingKey;

  // Rate limiter configuration
  private final int maxRequests = 60;
  private final int burstCapacity = 20;
  private final long windowSeconds = 60L;

  // Attack detection configuration
  private final int attackThreshold = 5;
  private final long attackWindowSeconds = 300L;
  private final long alertCooldownSeconds = 300L;
  private final long alertTokenTtlSeconds = 1800L;
  private final long blockDurationSeconds = 86400L; // 24 hours

  public int getMaxTokens() {
    return maxRequests;
  }

  /**
   * Checks whether the request for the given identifier (IP or username) is allowed.
   */
  public boolean isAllowed(String identifier) {
    if (isBlocked(identifier)) {
      return false;
    }

    String key = BUCKET_PREFIX + identifier;
    long now = System.currentTimeMillis();
    int capacity = maxRequests + burstCapacity;
    double refillRatePerMs = (double) maxRequests / (windowSeconds * 1000.0);
    long ttl = windowSeconds * 2;

    try {
      List<?> result =
          redisTemplate.execute(
              TOKEN_BUCKET_SCRIPT,
              List.of(key),
              String.valueOf(capacity),
              String.valueOf(refillRatePerMs),
              String.valueOf(now),
              "1",
              String.valueOf(ttl));

      if (result != null && !result.isEmpty()) {
        Number allowedNumber = (Number) result.get(0);
        return allowedNumber != null && allowedNumber.intValue() == 1;
      }
    } catch (Exception e) {
      log.error("[RATELIMIT] Error checking rate limit for {}: {}", identifier, e.getMessage());
    }
    return true;
  }

  /**
   * Checks if an identifier is currently blocked.
   */
  public boolean isBlocked(String identifier) {
    if (identifier == null || identifier.isBlank()) {
      return false;
    }
    return Boolean.TRUE.equals(redisTemplate.hasKey(BLOCK_PREFIX + identifier));
  }

  /**
   * Immediately blocks an identifier for the default duration (24h).
   */
  public void block(String identifier, String blockedBy) {
    block(identifier, blockedBy, blockDurationSeconds);
  }

  /**
   * Immediately blocks an identifier for the specified duration.
   */
  public void block(String identifier, String blockedBy, long durationSeconds) {
    if (identifier == null || identifier.isBlank()) {
      return;
    }
    redisTemplate
        .opsForValue()
        .set(BLOCK_PREFIX + identifier, blockedBy, durationSeconds, TimeUnit.SECONDS);
    log.warn(
        "[RATELIMIT] Identifier {} blocked by {} for {} seconds",
        identifier,
        blockedBy,
        durationSeconds);
  }

  /**
   * Returns remaining requests for the identifier.
   */
  public int remaining(String identifier) {
    try {
      Object raw = redisTemplate.opsForHash().get(BUCKET_PREFIX + identifier, "tokens");
      if (raw != null) {
        return (int) Math.floor(Double.parseDouble(raw.toString()));
      }
    } catch (Exception ignored) {
    }
    return maxRequests;
  }

  /**
   * Registers a rate-limit rejection and triggers an attack alert if the threshold is met.
   */
  public void registerRejection(String clientIp, String username) {
    String trackKey = ATTACK_PREFIX + clientIp;
    Long count = redisTemplate.opsForValue().increment(trackKey);
    if (count != null && count == 1) {
      redisTemplate.expire(trackKey, attackWindowSeconds, TimeUnit.SECONDS);
    }

    if (count != null && count >= attackThreshold) {
      triggerAttackAlert(clientIp, username, count);
    }
  }

  private void triggerAttackAlert(String clientIp, String username, long rejectionCount) {
    String cooldownKey = COOLDOWN_PREFIX + clientIp;
    Boolean isFirstAlert =
        redisTemplate.opsForValue().setIfAbsent(cooldownKey, "1", Duration.ofSeconds(alertCooldownSeconds));
    if (Boolean.FALSE.equals(isFirstAlert)) {
      return;
    }

    byte[] randomBytes = new byte[32];
    SECURE_RANDOM.nextBytes(randomBytes);
    String rawToken = Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    String fullToken = rawToken + "." + signToken(rawToken);

    String alertData = clientIp + "|" + (username != null ? username : "");
    redisTemplate
        .opsForValue()
        .set(TOKEN_PREFIX + rawToken, alertData, Duration.ofSeconds(alertTokenTtlSeconds));

    sendAttackAlertEmail(fullToken, clientIp, username, rejectionCount);
  }

  private void sendAttackAlertEmail(
      String fullToken, String clientIp, String username, long rejectionCount) {
    String recipient = getTargetAdminEmail();
    if (recipient == null || recipient.isBlank()) {
      log.warn("[RATELIMIT] No admin email configured in MailProperties. Skipping alert email.");
      return;
    }

    String blockIpUrl = serverBaseUrl + "/api/v1/ratelimit/alert/block?token=" + fullToken + "&choice=ip";
    String blockAccountUrl = serverBaseUrl + "/api/v1/ratelimit/alert/block?token=" + fullToken + "&choice=account";
    String blockBothUrl = serverBaseUrl + "/api/v1/ratelimit/alert/block?token=" + fullToken + "&choice=both";

    String userDisplay = (username != null && !username.isBlank())
        ? "<b>" + username + "</b>"
        : "<i>Unauthenticated / Public API</i>";

    String accountButtons = (username != null && !username.isBlank())
        ? """
          <a href="%s" style="display:inline-block;background-color:#f0ad4e;color:#fff;padding:10px 18px;text-decoration:none;border-radius:4px;font-weight:bold;margin-right:10px;margin-bottom:10px;">🛑 Block Account (%s)</a>
          <a href="%s" style="display:inline-block;background-color:#333;color:#fff;padding:10px 18px;text-decoration:none;border-radius:4px;font-weight:bold;margin-bottom:10px;">🛑 Block Both (IP & Account)</a>
          """.formatted(blockAccountUrl, username, blockBothUrl)
        : "";

    String html = """
        <div style="font-family:Arial,sans-serif;max-width:600px;margin:0 auto;padding:25px;border:1px solid #e1e4e8;border-radius:8px;background-color:#ffffff;">
          <h2 style="color:#d9534f;margin-top:0;">⚠️ Suspected Bot / Attack Detected</h2>
          <p style="color:#555;font-size:14px;">The rate limiter detected continuous rejected requests exceeding security thresholds.</p>
          <table style="width:100%;border-collapse:collapse;margin:20px 0;font-size:14px;">
            <tr><td style="padding:8px;border-bottom:1px solid #eee;font-weight:bold;width:140px;">Client IP:</td><td style="padding:8px;border-bottom:1px solid #eee;"><code>%s</code></td></tr>
            <tr><td style="padding:8px;border-bottom:1px solid #eee;font-weight:bold;">Account:</td><td style="padding:8px;border-bottom:1px solid #eee;">%s</td></tr>
            <tr><td style="padding:8px;border-bottom:1px solid #eee;font-weight:bold;">Rejections (last 5 min):</td><td style="padding:8px;border-bottom:1px solid #eee;color:#d9534f;font-weight:bold;">%d</td></tr>
          </table>
          <h3 style="margin-top:25px;color:#333;">Choose Action to Block:</h3>
          <p style="font-size:14px;color:#555;">Click an option below to immediately block the target for 24 hours without logging in:</p>
          <div style="margin:20px 0;">
            <a href="%s" style="display:inline-block;background-color:#d9534f;color:#fff;padding:10px 18px;text-decoration:none;border-radius:4px;font-weight:bold;margin-right:10px;margin-bottom:10px;">🛑 Block IP (%s)</a>
            %s
          </div>
          <p style="color:#888;font-size:12px;margin-top:25px;">This signed one-time link is valid for 30 minutes. If this was a false alarm, you can safely ignore this email.</p>
        </div>
        """.formatted(clientIp, userDisplay, rejectionCount, blockIpUrl, clientIp, accountButtons);

    EmailRequest emailRequest =
        EmailRequest.html(List.of(recipient), html, "🚨 [SECURITY ALERT] Rate Limit Attack Detected on Inlaco Service")
            .build();

    try {
      emailSender.sendNotificationAsync(emailRequest);
      log.info("[RATELIMIT] Attack alert email dispatched to {} for IP={} user={}", recipient, clientIp, username);
    } catch (Exception e) {
      log.error("[RATELIMIT] Failed to dispatch attack alert email: {}", e.getMessage(), e);
    }
  }

  /**
   * Consumes a one-time signed token from email and executes the chosen block action.
   */
  public String executeBlockChoice(String fullToken, String choice, String callerIp) {
    if (fullToken == null || !fullToken.contains(".")) {
      recordFailedAlertAttempt(callerIp);
      return renderHtmlResult("Access Denied", "Invalid or forged security token.", false);
    }

    String[] parts = fullToken.split("\\.", 2);
    String rawToken = parts[0];
    String signature = parts[1];

    if (!verifySignature(rawToken, signature)) {
      recordFailedAlertAttempt(callerIp);
      log.warn("[SECURITY] Forged alert token signature from caller IP={}", callerIp);
      return renderHtmlResult("Access Denied", "Cryptographic signature verification failed.", false);
    }

    // Atomic burn-on-read
    String key = TOKEN_PREFIX + rawToken;
    String data = redisTemplate.opsForValue().getAndDelete(key);
    if (data == null) {
      return renderHtmlResult("Link Expired or Already Used", "This security alert link has expired or has already been used.", false);
    }

    String[] alertDetails = data.split("\\|", 2);
    String ip = alertDetails.length > 0 ? alertDetails[0] : "";
    String username = alertDetails.length > 1 ? alertDetails[1] : "";

    StringBuilder blockedDetails = new StringBuilder();
    boolean blockedAny = false;

    if ("ip".equalsIgnoreCase(choice) || "both".equalsIgnoreCase(choice)) {
      if (!ip.isBlank()) {
        block(ip, "Admin via Alert Link (" + choice + ")");
        blockedDetails.append("<li>Blocked IP: <b>").append(ip).append("</b></li>");
        blockedAny = true;
      }
    }

    if ("account".equalsIgnoreCase(choice) || "both".equalsIgnoreCase(choice)) {
      if (!username.isBlank()) {
        block(username, "Admin via Alert Link (" + choice + ")");
        blockedDetails.append("<li>Blocked Account: <b>").append(username).append("</b></li>");
        blockedAny = true;
      } else if (!"both".equalsIgnoreCase(choice)) {
        return renderHtmlResult("No Account Associated", "This attack occurred without an account. Please block the IP address instead.", false);
      }
    }

    if (!blockedAny) {
      return renderHtmlResult("Block Failed", "No valid identifier found to block.", false);
    }

    return renderHtmlResult(
        "Successfully Blocked",
        "<p>The following targets have been blocked for 24 hours:</p><ul>" + blockedDetails + "</ul>",
        true);
  }

  private void recordFailedAlertAttempt(String callerIp) {
    if (callerIp == null || callerIp.isBlank()) {
      return;
    }
    String key = FAILED_ATTEMPTS_PREFIX + callerIp;
    Long attempts = redisTemplate.opsForValue().increment(key);
    if (attempts != null && attempts == 1) {
      redisTemplate.expire(key, 300, TimeUnit.SECONDS);
    }
    if (attempts != null && attempts >= 3) {
      log.error("[SECURITY] Potential probe on alert endpoint from IP={}. Blocking IP immediately.", callerIp);
      block(callerIp, "Probing Alert Block Endpoint", blockDurationSeconds);
    }
  }

  private String signToken(String rawToken) {
    try {
      Mac mac = Mac.getInstance("HmacSHA256");
      mac.init(new SecretKeySpec(getSigningKeyBytes(), "HmacSHA256"));
      byte[] hmacBytes = mac.doFinal(rawToken.getBytes(StandardCharsets.UTF_8));
      return Base64.getUrlEncoder().withoutPadding().encodeToString(hmacBytes);
    } catch (Exception e) {
      throw new RuntimeException("Failed to sign alert token", e);
    }
  }

  private boolean verifySignature(String rawToken, String expectedSignature) {
    try {
      String computedSignature = signToken(rawToken);
      return MessageDigest.isEqual(
          computedSignature.getBytes(StandardCharsets.UTF_8),
          expectedSignature.getBytes(StandardCharsets.UTF_8));
    } catch (Exception e) {
      log.error("[SECURITY] Error verifying alert token signature: {}", e.getMessage());
      return false;
    }
  }

  private byte[] getSigningKeyBytes() {
    String key = (signingKey != null && !signingKey.isBlank())
        ? signingKey
        : "inlaco-ratelimit-secure-signing-key-32bytes";
    return key.getBytes(StandardCharsets.UTF_8);
  }

  private String renderHtmlResult(String title, String message, boolean success) {
    String color = success ? "#28a745" : "#d9534f";
    String icon = success ? "✅" : "⚠️";
    return """
        <!DOCTYPE html>
        <html>
        <head>
          <meta charset="utf-8"/>
          <title>%s</title>
          <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
        </head>
        <body style="font-family:-apple-system,BlinkMacSystemFont,'Segoe UI',Roboto,Helvetica,Arial,sans-serif;background-color:#f6f8fa;margin:0;padding:40px 20px;">
          <div style="max-width:550px;margin:0 auto;background:#ffffff;border-radius:8px;box-shadow:0 2px 8px rgba(0,0,0,0.08);padding:30px;border-top:5px solid %s;">
            <h2 style="color:%s;margin-top:0;">%s %s</h2>
            <div style="color:#333;font-size:15px;line-height:1.6;">%s</div>
            <hr style="border:none;border-top:1px solid #eee;margin:25px 0;"/>
            <p style="color:#888;font-size:13px;margin:0;">Inlaco Crew Management Security System</p>
          </div>
        </body>
        </html>
        """.formatted(title, color, color, icon, title, message);
  }

  private String getTargetAdminEmail() {
    if (mailProperties != null && mailProperties.getUsername() != null && !mailProperties.getUsername().isBlank()) {
      return mailProperties.getUsername();
    }
    return null;
  }
}

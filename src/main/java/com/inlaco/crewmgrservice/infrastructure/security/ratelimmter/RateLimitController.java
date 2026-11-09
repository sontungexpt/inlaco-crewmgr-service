package com.inlaco.crewmgrservice.infrastructure.security.ratelimmter;

import com.inlaco.crewmgrservice.infrastructure.web.annotation.PublicEndpoint;
import com.inlaco.crewmgrservice.infrastructure.web.util.HttpHeaderUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/ratelimit")
@RequiredArgsConstructor
public class RateLimitController {

  private final RateLimiterService rateLimiter;

  /**
   * Immediately blocks an identifier (username or IP) from making further requests.
   * Used by logged-in admin.
   */
  @PostMapping("/block")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<Void> block(
      @RequestParam @NotBlank(message = "identifier is required") String identifier) {
    rateLimiter.block(identifier, "admin");
    log.info("Admin blocked identifier: {}", identifier);
    return ResponseEntity.noContent().build();
  }

  /**
   * Public endpoint clicked directly from the attack alert email in Gmail.
   * Does NOT require user login, but is cryptographically secured via dual-layer
   * HMAC-signed one-time token bound strictly to the attacked target.
   * Any unauthorized or forged attempts are automatically blocked.
   */
  @GetMapping(value = "/alert/block", produces = MediaType.TEXT_HTML_VALUE)
  @PublicEndpoint
  public ResponseEntity<String> blockFromAlert(
      HttpServletRequest request,
      @RequestParam("token") String token,
      @RequestParam(value = "choice", defaultValue = "ip") String choice) {
    String callerIp = HttpHeaderUtils.getClientIp(request);
    String htmlResult = rateLimiter.executeBlockChoice(token, choice, callerIp);
    return ResponseEntity.ok(htmlResult);
  }
}

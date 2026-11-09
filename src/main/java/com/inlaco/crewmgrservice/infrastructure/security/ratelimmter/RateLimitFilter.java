package com.inlaco.crewmgrservice.infrastructure.security.ratelimmter;

import com.inlaco.crewmgrservice.feature.auth.infrastructure.security.SecurityUser;
import com.inlaco.crewmgrservice.infrastructure.web.util.HttpHeaderUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Component
@RequiredArgsConstructor
public class RateLimitFilter extends OncePerRequestFilter {

  private static final String ALERT_BLOCK_PATH = "/api/v1/ratelimit/alert/block";

  private final RateLimiterService rateLimiter;

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) {
    String path = request.getRequestURI();
    // Exclude the email alert callback itself so admins are never blocked from clicking the alert link
    return path.equals(ALERT_BLOCK_PATH) || path.startsWith(ALERT_BLOCK_PATH + "/");
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    String path = request.getRequestURI();
    String clientIp = HttpHeaderUtils.getClientIp(request);

    // Determine authenticated username if present
    String username = null;
    try {
      var auth = SecurityContextHolder.getContext().getAuthentication();
      boolean isAuthenticated =
          auth != null
              && auth.isAuthenticated()
              && !(auth.getPrincipal() instanceof AnonymousAuthenticationToken);
      if (isAuthenticated && auth.getPrincipal() instanceof SecurityUser su) {
        username = su.user().getUsername();
      }
    } catch (Exception ignored) {
    }

    // 1. Blocked checks
    if (rateLimiter.isBlocked(clientIp)) {
      log.warn("[RATELIMIT] Blocked IP rejected: ip={} path={}", clientIp, path);
      respondBlocked(response, request, clientIp);
      return;
    }

    if (username != null && rateLimiter.isBlocked(username)) {
      log.warn("[RATELIMIT] Blocked user rejected: user={} ip={} path={}", username, clientIp, path);
      respondBlocked(response, request, username);
      return;
    }

    // 2. Rate limit evaluation (applied to ALL endpoints including login, register, and public APIs)
    String primaryIdentifier = (username != null) ? username : clientIp;

    boolean allowed = rateLimiter.isAllowed(primaryIdentifier);
    int remaining = rateLimiter.remaining(primaryIdentifier);

    // If request was authenticated, also verify IP rate limit to prevent bot farms reusing single IP
    if (allowed && username != null) {
      allowed = rateLimiter.isAllowed(clientIp);
      remaining = Math.min(remaining, rateLimiter.remaining(clientIp));
    }

    response.setHeader("X-Rate-Limit-Limit", String.valueOf(rateLimiter.getMaxTokens()));
    response.setHeader("X-Rate-Limit-Remaining", String.valueOf(remaining));

    if (!allowed) {
      log.warn(
          "[RATELIMIT] Limit exceeded identifier={} ip={} username={} method={} path={}",
          primaryIdentifier,
          clientIp,
          username,
          request.getMethod(),
          path);

      // Register rejection for attack detection and email alert
      rateLimiter.registerRejection(clientIp, username);

      respondTooManyRequests(response, request, remaining);
      return;
    }

    filterChain.doFilter(request, response);
  }

  private void respondTooManyRequests(
      HttpServletResponse response, HttpServletRequest request, int remaining) throws IOException {
    response.setHeader("Retry-After", "60");
    response.setHeader("X-Rate-Limit-Retry-After-Seconds", "60");
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
    response.getWriter().write(
        "{\"error\":\"RATE_LIMIT_EXCEEDED\",\"message\":\"Too many requests, please try again later\"}");
  }

  private void respondBlocked(
      HttpServletResponse response, HttpServletRequest request, String identifier) throws IOException {
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
    response.getWriter().write(
        "{\"error\":\"IDENTIFIER_BLOCKED\",\"message\":\"This identifier has been blocked due to suspected abuse\",\"identifier\":\""
            + identifier
            + "\"}");
  }
}

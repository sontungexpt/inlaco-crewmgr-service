package com.inlaco.crewmgrservice.infrastructure.security.config;

import com.inlaco.crewmgrservice.infrastructure.security.access.PublicEndpointResolver;
import jakarta.servlet.http.HttpServletRequest;
import java.util.function.Supplier;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.authorization.AuthorizationResult;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class ApiEndpointAuthorizationManager
    implements AuthorizationManager<RequestAuthorizationContext> {

  private final PublicEndpointResolver publicEndpointResolver;

  @Override
  public @Nullable AuthorizationResult authorize(
      Supplier<? extends @Nullable Authentication> authentication,
      RequestAuthorizationContext context) {
    HttpServletRequest request = context.getRequest();

    String uri = request.getRequestURI();
    String method = request.getMethod();

    log.debug("[AUTH] Incoming request: {} {}", method, uri);

    // Public endpoints
    if (publicEndpointResolver.isPublic(request)) {
      log.debug("[AUTH] -> PUBLIC: {} {}", method, uri);
      return new AuthorizationDecision(true);
    }

    // Auth required
    Authentication auth = authentication.get();
    boolean granted =
        auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken);

    log.debug(
        "[AUTH] -> AUTH REQUIRED: {} {}, authenticated={}, principal={}",
        method,
        uri,
        granted,
        auth != null ? auth.getClass().getSimpleName() : "null");

    return new AuthorizationDecision(granted);
  }
}

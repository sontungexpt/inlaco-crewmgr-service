package com.inlaco.crewmgrservice.feature.apikey.infrastructure.security;

import com.inlaco.crewmgrservice.feature.apikey.application.port.in.ApiKeyUseCase;
import com.inlaco.crewmgrservice.feature.apikey.domain.model.ApiKey;
import com.inlaco.crewmgrservice.feature.apikey.infrastructure.config.ApiKeyConfig;
import com.inlaco.crewmgrservice.feature.auth.infrastructure.security.AuthorityResolver;
import com.inlaco.crewmgrservice.feature.auth.infrastructure.security.SecurityUser;
import com.inlaco.crewmgrservice.feature.user.application.port.in.UserUseCase;
import com.inlaco.crewmgrservice.feature.user.domain.model.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Slf4j
@Component
public class ApiKeyAuthenticationFilter extends OncePerRequestFilter {

  private final HandlerExceptionResolver exceptionResolver;
  private final UserUseCase userUseCase;
  private final AuthorityResolver authorityResolver;
  private final ApiKeyUseCase apiKeyUseCase;
  private final ApiKeyConfig config;

  public ApiKeyAuthenticationFilter(
      UserUseCase userUseCase,
      AuthorityResolver authorityResolver,
      ApiKeyUseCase apiKeyUseCase,
      ApiKeyConfig config,
      @Qualifier("handlerExceptionResolver") HandlerExceptionResolver exceptionResolver) {
    this.exceptionResolver = exceptionResolver;
    this.userUseCase = userUseCase;
    this.authorityResolver = authorityResolver;
    this.apiKeyUseCase = apiKeyUseCase;
    this.config = config;
  }

  private final String getApiKeyId(HttpServletRequest request) {
    return request.getHeader(config.getHeaders().getKeyId());
  }

  private final String getApiKeySecret(HttpServletRequest request) {
    return request.getHeader(config.getHeaders().getKeySecret());
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    String apiKeyId = getApiKeyId(request);
    String apiKeySecret = getApiKeySecret(request);

    if (apiKeyId != null && apiKeySecret != null) {
      log.debug("Attempting API key authentication for key ID: {}", apiKeyId);

      ApiKey apiKey = apiKeyUseCase.validateApiKey(apiKeyId, apiKeySecret);

      if (apiKey != null) {
        log.debug("API key authentication successful for client: {}", apiKey.getClientName());

        User user = userUseCase.findById(apiKey.getCreatedBy());
        SecurityUser su = new SecurityUser(user, authorityResolver.resolve(user));

        log.debug("[JWT-AUTH] Authorities resolved for userId={}", user.getId());

        UsernamePasswordAuthenticationToken auth =
            new UsernamePasswordAuthenticationToken(su, null, su.getAuthorities());

        auth.setDetails(request);

        SecurityContextHolder.getContext().setAuthentication(auth);
      } else {
        log.warn("API key authentication failed for key ID: {}", apiKeyId);
      }
    }

    filterChain.doFilter(request, response);
  }

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) {
    // Don't filter if no API key headers are present
    if (getApiKeyId(request) == null || getApiKeySecret(request) == null) {
      log.debug("No API key headers present in request");
      return true;
    }

    String path = request.getRequestURI();
    // Don't filter for auth and public endpoints
    return path.startsWith("/swagger-ui/")
        || path.startsWith("/v3/api-docs/")
        || path.startsWith("/actuator/");
  }
}

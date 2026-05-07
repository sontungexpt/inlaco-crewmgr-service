package com.inlaco.crewmgrservice.feature.apikey.infrastructure.security;

import com.inlaco.crewmgrservice.feature.apikey.application.port.in.ApiKeyUseCase;
import com.inlaco.crewmgrservice.feature.apikey.domain.model.ApiKey;
import com.inlaco.crewmgrservice.feature.apikey.infrastructure.config.ApiKeyConfig;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Component
@RequiredArgsConstructor
public class ApiKeyAuthenticationFilter extends OncePerRequestFilter {

  private final ApiKeyUseCase apiKeyUseCase;
  private final ApiKeyConfig config;

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

        UsernamePasswordAuthenticationToken authentication =
            new UsernamePasswordAuthenticationToken(apiKey.getClientName(), null, null);
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authentication);
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
      return true;
    }

    String path = request.getRequestURI();
    // Don't filter for auth and public endpoints
    return path.startsWith("/swagger-ui/")
        || path.startsWith("/v3/api-docs/")
        || path.startsWith("/actuator/");
  }
}

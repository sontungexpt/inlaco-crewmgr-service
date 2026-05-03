package com.inlaco.crewmgrservice.infrastructure.security.jwt.filter;

import com.inlaco.crewmgrservice.infrastructure.security.access.PublicEndpointResolver;
import com.inlaco.crewmgrservice.infrastructure.security.auth.TokenAuthenticator;
import com.inlaco.crewmgrservice.infrastructure.security.jwt.exception.JwtTokenException;
import com.inlaco.crewmgrservice.infrastructure.web.util.HttpHeaderUtils;
import com.inlaco.crewmgrservice.shared.kernel.exception.ResourceNotFoundException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final HandlerExceptionResolver exceptionResolver;
  private final PublicEndpointResolver publicEndpointResolver;
  private final TokenAuthenticator tokenAuthenticator;

  public JwtAuthenticationFilter(
      PublicEndpointResolver publicEndpointResolver,
      TokenAuthenticator tokenAuthenticator,
      @Qualifier("handlerExceptionResolver") HandlerExceptionResolver exceptionResolver) {
    this.exceptionResolver = exceptionResolver;
    this.tokenAuthenticator = tokenAuthenticator;
    this.publicEndpointResolver = publicEndpointResolver;
  }

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
    boolean fullyPublic = publicEndpointResolver.isFullyPublic(request);
    log.info("[JWT] Checking if request should be filtered: fullyPublic={}", fullyPublic);
    return fullyPublic;
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    try {
      log.debug("[JWT] Starting authentication process for request: {}", request.getRequestURI());
      Authentication existingAuth = SecurityContextHolder.getContext().getAuthentication();
      // Already authenticated
      if (existingAuth != null && !isAnonymous(existingAuth)) {
        log.debug(
            "[JWT] User already authenticated, skipping filter for request: {}",
            request.getRequestURI());
        log.info(
            "[JWT] Authentication successful, proceeding with filter chain for request: {}",
            request.getRequestURI());
        filterChain.doFilter(request, response);
        return;
      }

      HttpHeaderUtils.extractBearerToken(request).ifPresent(token -> authenticate(token, request));
      filterChain.doFilter(request, response);
    } catch (JwtTokenException ex) {
      log.error(
          "[JWT] Authentication failed for request: {}. Error: {}",
          request.getRequestURI(),
          ex.getMessage());
      exceptionResolver.resolveException(request, response, null, ex);
    }
  }

  /* ==========================================================
   * Core authentication logic
   * ========================================================== */
  private void authenticate(String token, HttpServletRequest request) throws JwtTokenException {
    try {
      log.info("[JWT] Attempting authentication for request: {}", request.getRequestURI());
      Authentication auth =
          tokenAuthenticator.authenticate(
              token, new WebAuthenticationDetailsSource().buildDetails(request));
      SecurityContextHolder.getContext().setAuthentication(auth);
      log.info("[JWT] Authentication successful for request: {}", request.getRequestURI());
    } catch (ResourceNotFoundException e) {
      log.warn("[JWT] Invalid token. Error: {}", e.getMessage());
      throw new JwtTokenException(token, "Malformed jwt token");
    }
  }

  private boolean isAnonymous(Authentication authentication) {
    return authentication instanceof AnonymousAuthenticationToken;
  }
}

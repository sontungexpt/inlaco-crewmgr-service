package com.inlaco.crewmgrservice.infrastructure.security.jwt.filter;

import com.inlaco.crewmgrservice.feature.user.model.SecurityUser;
import com.inlaco.crewmgrservice.feature.user.repository.UserRepository;
import com.inlaco.crewmgrservice.feature.user.service.AuthorityResolver;
import com.inlaco.crewmgrservice.infrastructure.security.jwt.exception.JwtTokenException;
import com.inlaco.crewmgrservice.infrastructure.security.jwt.service.JwtAccessTokenService;
import com.inlaco.crewmgrservice.utils.HttpHeaderUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
  private final JwtAccessTokenService jwtTokenService;
  private final UserRepository userRepository;
  private final HandlerExceptionResolver exceptionResolver;
  private final AuthorityResolver authorityResolver;

  public JwtAuthenticationFilter(
      JwtAccessTokenService jwtService,
      UserRepository userRepository,
      AuthorityResolver authorityResolver,
      @Qualifier("handlerExceptionResolver") HandlerExceptionResolver exceptionResolver) {
    this.jwtTokenService = jwtService;
    this.userRepository = userRepository;
    this.exceptionResolver = exceptionResolver;
    this.authorityResolver = authorityResolver;
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    try {
      Authentication existingAuth = SecurityContextHolder.getContext().getAuthentication();
      // Already authenticated
      if (existingAuth != null && !isAnonymous(existingAuth)) {
        filterChain.doFilter(request, response);
        return;
      }

      HttpHeaderUtils.extractBearerToken(request).ifPresent(token -> authenticate(token, request));
      filterChain.doFilter(request, response);

    } catch (JwtTokenException ex) {
      log.warn("[JWT] Authentication failed: {}", ex.getMessage());
      exceptionResolver.resolveException(request, response, null, ex);
    }
  }

  /* ==========================================================
   * Core authentication logic
   * ========================================================== */
  private void authenticate(String token, HttpServletRequest request) throws JwtTokenException {
    String pubId = jwtTokenService.parseSubject(token);
    userRepository
        .findByPubId(pubId)
        .ifPresent(
            user -> {
              SecurityUser su = new SecurityUser(user, authorityResolver);
              var auth = new UsernamePasswordAuthenticationToken(su, null, su.getAuthorities());
              auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
              SecurityContextHolder.getContext().setAuthentication(auth);
              log.debug("[JWT] User authenticated: pubId={}", pubId);
            });
  }

  private boolean isAnonymous(Authentication authentication) {
    return authentication instanceof AnonymousAuthenticationToken;
  }
}

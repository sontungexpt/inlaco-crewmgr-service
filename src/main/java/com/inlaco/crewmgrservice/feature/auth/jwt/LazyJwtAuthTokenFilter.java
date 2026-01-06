package com.inlaco.crewmgrservice.feature.auth.jwt;

import com.inlaco.crewmgrservice.exceptions.JwtTokenException;
import com.inlaco.crewmgrservice.feature.user.model.User;
import com.inlaco.crewmgrservice.feature.user.repository.UserRepository;
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
public class LazyJwtAuthTokenFilter extends OncePerRequestFilter {
  private final JwtService jwtService;
  private final UserRepository userRepository;
  private final HandlerExceptionResolver exceptionResolver;

  public LazyJwtAuthTokenFilter(
      JwtService jwtService,
      UserRepository userRepository,
      @Qualifier("handlerExceptionResolver") HandlerExceptionResolver exceptionResolver) {

    this.jwtService = jwtService;
    this.userRepository = userRepository;
    this.exceptionResolver = exceptionResolver;
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
    String userPubId = jwtService.extractSubject(token);
    if (userPubId == null) {
      throw new JwtTokenException(token, "Invalid JWT subject");
    }

    User user =
        userRepository
            .findByPubId(userPubId)
            .orElseThrow(() -> new JwtTokenException(token, "User not found"));

    if (!jwtService.isAccessTokenValid(token, user)) {
      throw new JwtTokenException(token, "Invalid or expired JWT");
    }

    var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

    SecurityContextHolder.getContext().setAuthentication(authentication);

    log.debug("[JWT] User authenticated: pubId={}", userPubId);
  }

  private boolean isAnonymous(Authentication authentication) {
    return authentication instanceof AnonymousAuthenticationToken;
  }
}

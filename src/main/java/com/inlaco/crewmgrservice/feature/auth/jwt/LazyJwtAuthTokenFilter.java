package com.inlaco.crewmgrservice.feature.auth.jwt;

import com.inlaco.crewmgrservice.exceptions.JwtTokenException;
import com.inlaco.crewmgrservice.feature.user.model.User;
import com.inlaco.crewmgrservice.feature.user.repository.UserRepository;
import com.inlaco.crewmgrservice.utils.ApiEndpointSecurityInspector;
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
  private final ApiEndpointSecurityInspector endpointInspector;
  private final HandlerExceptionResolver exceptionResolver;

  public LazyJwtAuthTokenFilter(
      JwtService jwtService,
      UserRepository userRepository,
      ApiEndpointSecurityInspector endpointInspector,
      @Qualifier("handlerExceptionResolver") HandlerExceptionResolver exceptionResolver) {

    this.jwtService = jwtService;
    this.userRepository = userRepository;
    this.endpointInspector = endpointInspector;
    this.exceptionResolver = exceptionResolver;
  }

  /* ==========================================================
   * Skip filter for public no-JWT endpoints
   * ========================================================== */
  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) {
    boolean skip = endpointInspector.isUnsecureJwtRequest(request);
    log.debug("[JWT] {} skip filter = {}", request.getRequestURI(), skip);
    return skip;
  }

  /* ==========================================================
   * JWT processing
   * ========================================================== */
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

      String jwtToken = HttpHeaderUtils.extractBearerToken(request).orElse(null);

      if (jwtToken == null) {
        boolean optionalJwt = endpointInspector.isOptionalJwtSecurityPath(request);
        if (optionalJwt) {
          filterChain.doFilter(request, response);
          return;
        }
        throw new JwtTokenException("Missing JWT token");
      }

      authenticate(jwtToken, request);
      filterChain.doFilter(request, response);

    } catch (Exception ex) {
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

    UsernamePasswordAuthenticationToken authentication =
        new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

    SecurityContextHolder.getContext().setAuthentication(authentication);

    log.debug("[JWT] User authenticated: pubId={}", userPubId);
  }

  //   private boolean isAnonymous(Authentication authentication) {
  //     return authentication.getName() == "anonymousUser";
  //   }

  private boolean isAnonymous(Authentication authentication) {
    return authentication instanceof AnonymousAuthenticationToken;
  }
}

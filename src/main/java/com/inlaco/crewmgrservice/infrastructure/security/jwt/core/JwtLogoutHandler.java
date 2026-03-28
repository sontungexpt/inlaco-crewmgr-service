package com.inlaco.crewmgrservice.infrastructure.security.jwt.core;

import com.inlaco.crewmgrservice.feature.auth.application.port.in.LogoutUseCase;
import com.inlaco.crewmgrservice.infrastructure.web.util.HttpHeaderUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Service
@Slf4j
public class JwtLogoutHandler implements LogoutHandler {

  private final LogoutUseCase logoutUseCase;
  private final HandlerExceptionResolver resolver;

  public JwtLogoutHandler(
      @Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver,
      LogoutUseCase logoutUseCase) {
    this.resolver = resolver;
    this.logoutUseCase = logoutUseCase;
  }

  @Override
  public void logout(
      HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
    try {
      String refreshToken = HttpHeaderUtils.extractBearerTokenOrThrow(request);
      logoutUseCase.logout(refreshToken);
      response.setStatus(HttpStatus.NO_CONTENT.value());
    } catch (Exception e) {
      log.debug("Logout failed", e);
      resolver.resolveException(request, response, null, e);
    }
  }
}

package com.inlaco.crewmgrservice.feature.auth.jwt;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Slf4j
@Component
public record AuthEntryPointJwt(
    @Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver)
    implements AuthenticationEntryPoint {

  @Override
  public void commence(
      HttpServletRequest request, HttpServletResponse response, AuthenticationException ex)
      throws IOException {
    log.info("User is unauthorised. Routing from the entry point {}", ex.getMessage());

    resolver.resolveException(request, response, null, ex);
  }
}

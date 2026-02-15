package com.inlaco.crewmgrservice.infrastructure.web.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Optional;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class HttpServletUtils {

  public static Optional<HttpServletRequest> getRequest() {
    RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
    return Optional.ofNullable((ServletRequestAttributes) attributes)
        .map(ServletRequestAttributes::getRequest);
  }

  public static Optional<HttpServletResponse> getResponse() {
    RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
    return Optional.ofNullable((ServletRequestAttributes) attributes)
        .map(ServletRequestAttributes::getResponse);
  }
}

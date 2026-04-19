package com.inlaco.crewmgrservice.infrastructure.web.filter;

import com.inlaco.crewmgrservice.shared.constant.MDCContextKey;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;
import tools.jackson.databind.ObjectMapper;

@Component
@RequiredArgsConstructor
@Slf4j
@Order(Ordered.LOWEST_PRECEDENCE - 1)
public class HttpLoggingFilter extends OncePerRequestFilter {

  private final ObjectMapper mapper;

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    long start = System.currentTimeMillis();

    boolean isDebug = log.isDebugEnabled();

    HttpServletRequest req = request;
    HttpServletResponse res = response;

    // chỉ wrap khi cần debug (tránh overhead)
    if (isDebug) {
      req = new ContentCachingRequestWrapper(request, 8192);
      res = new ContentCachingResponseWrapper(response);
    }

    try {
      filterChain.doFilter(req, res);
    } finally {
      long duration = System.currentTimeMillis() - start;

      log.info(
          "HTTP {} {}?{} status={} duration={}ms trace={} clientIp={}",
          request.getMethod(),
          request.getRequestURI(),
          request.getQueryString(),
          response.getStatus(),
          duration,
          MDC.get(MDCContextKey.TRACE_ID),
          MDC.get(MDCContextKey.CLIENT_IP));

      // ===== DEBUG → log full =====
      if (isDebug
          && req instanceof ContentCachingRequestWrapper
          && res instanceof ContentCachingResponseWrapper) {
        log.debug(
            buildFullLog(
                (ContentCachingRequestWrapper) req, (ContentCachingResponseWrapper) res, duration));
        ((ContentCachingResponseWrapper) res).copyBodyToResponse();
      }
    }
  }

  private String buildFullLog(
      ContentCachingRequestWrapper request, ContentCachingResponseWrapper response, long duration) {
    StringBuilder logMessage = new StringBuilder();

    logMessage
        .append("\n========== HTTP DEBUG ==========\n")
        .append("TIME        : ")
        .append(Instant.now())
        .append("\n")
        .append("METHOD      : ")
        .append(request.getMethod())
        .append("\n")
        .append("URI         : ")
        .append(request.getRequestURI())
        .append("\n")
        .append("QUERY       : ")
        .append(request.getQueryString())
        .append("\n")
        .append("STATUS      : ")
        .append(response.getStatus())
        .append("\n")
        .append("DURATION    : ")
        .append(duration)
        .append(" ms\n")
        .append("TRACE       : ")
        .append(MDC.get(MDCContextKey.TRACE_ID))
        .append("\n")
        .append("CLIENT IP   : ")
        .append(MDC.get(MDCContextKey.CLIENT_IP))
        .append("\n");

    // headers
    logMessage.append("\nHEADERS:\n");
    Collections.list(request.getHeaderNames())
        .forEach(
            header ->
                logMessage
                    .append("  ")
                    .append(header)
                    .append(" : ")
                    .append(maskIfSensitive(header, request.getHeader(header)))
                    .append("\n"));

    // body
    logMessage.append("\nREQUEST BODY:\n").append(getRequestBody(request));

    logMessage.append("\n\nRESPONSE BODY:\n").append(getResponseBody(response));

    logMessage.append("\n========== END ==========\n");

    return logMessage.toString();
  }

  private String getRequestBody(ContentCachingRequestWrapper request) {
    byte[] content = request.getContentAsByteArray();
    if (content.length == 0) return "<empty>";

    String body = new String(content, StandardCharsets.UTF_8);
    return prettyIfJson(body);
  }

  private String getResponseBody(ContentCachingResponseWrapper response) {
    byte[] content = response.getContentAsByteArray();
    if (content.length == 0) return "<empty>";

    String body = new String(content, StandardCharsets.UTF_8);
    return prettyIfJson(body);
  }

  private String maskIfSensitive(String header, String value) {
    if (header == null) return "";
    if ("authorization".equalsIgnoreCase(header)) {
      return "***MASKED***";
    }
    return value;
  }

  private String prettyIfJson(String body) {
    try {
      Object json = mapper.readValue(body, Object.class);
      return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
    } catch (Exception e) {
      return body;
    }
  }
}
